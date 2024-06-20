import requests
import pandas as pd
import aiohttp
import asyncio
from bs4 import BeautifulSoup
import time
import csv
import re

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import ElementClickInterceptedException, StaleElementReferenceException, TimeoutException

# konfig przeglądarki
firefox_options = webdriver.FirefoxOptions()
firefox_options.add_argument("--headless")
driver = webdriver.Firefox(options=firefox_options)
driver.implicitly_wait(10)  # Ustawienie domyślnego czasu oczekiwania

no_description = "Ta roślina nie posiada opisu"
no_image = "https://e-katalogroslin.pl/wp-content/themes/e-katalogroslin/img/e-kat_noimg.jpg"
default_img = "default_plant.jpg"

url_list = [
"https://e-katalogroslin.pl/przegladaj-katalog?se=b873c55f322d2ae7c077b52480a75f81",
"https://e-katalogroslin.pl/przegladaj-katalog?se=39921be9413de6ac538a4df45da7d104",
"https://e-katalogroslin.pl/przegladaj-katalog?se=0f53540eb1787eab5cf47d55874e4372",
"https://e-katalogroslin.pl/przegladaj-katalog?se=04c2fcb871ee0704e7af0cd6183a01e1",
"https://e-katalogroslin.pl/przegladaj-katalog?se=85f76f2e4609984bd0caa1a567285df3"]

banned_properties = ['pochodzenie', 'zasieg_geograficzny', 'strefa']

last_url = "https://e-katalogroslin.pl/przegladaj-katalog?se=60e5c0024c14f99ceaa4840aa4d02bd1"

def remove_polish(str):
    return str.lower().replace('ł', 'l').replace('ą', 'a').replace('ć', 'c').replace('ę', 'e').replace('ś', 's').replace('ń', 'n').replace('ó', 'o').replace('ż', 'z').replace('ź', 'z')

def clean_description(description):
    description = description.strip().replace('"', "'").replace('\t', '\n')

    # usuwanie znaków specjalnych
    cleaned_description = re.sub(r'[^\w\s]', '', description)
    # usuwanie nadmiarowych spacji pomiedzy wierszami
    cleaned_description = re.sub(r'\s+', ' ', cleaned_description)
    return cleaned_description

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.text()

async def get_plant_properties(session, plant_url):
    html = await fetch(session, plant_url)
    soup = BeautifulSoup(html, 'html.parser')
    table_rows = soup.select('table.product_details_table tr')
    plant_properties = {}
    desc = soup.find('p', {"class": "description"}).get_text()
    desc = clean_description(desc)
    plant_properties["opis"] = desc
    
    for row in table_rows:
        columns = row.find_all('td')
        if len(columns) == 2:
            property_name = remove_polish(columns[0].text.strip().replace(':', '').replace(' ', '_').lower())
            if(property_name in banned_properties):
                continue
            property_value = columns[1].get_text(separator=', ', strip=True)
            
            if(property_name in "docelowa_wysokosc"):
                property_value = re.sub(r'(\d+,\d+)', lambda match: match.group(0).replace(',', '.'), property_value)
            
            plant_properties[property_name] = property_value

    return plant_properties

async def parse_page(html):
    soup = BeautifulSoup(html, 'html.parser')
    listing_section = soup.find('section', id='listning')
    elements = listing_section.find_all('div', class_='element')
    plant_info_list = []

    for element in elements:
        desc = element.find('div', class_='description').find('p', class_='desc_desc').get_text(strip=True)
        if desc == no_description:
            continue
        
        name_pp = element.find('div', class_='description').find('p', class_='desc_pl_title').find("span")
        latin_name_pp = element.find('div', class_='description').find('p', class_='desc_title')

        def trace_spanowanie(pp):
            if pp:
                name = ""
                for content in pp.contents:
                    if isinstance(content, str):
                        name += content.strip() + " "
                    elif content.name == 'span':
                        if content.get('class') == ['i']:
                            name += content.get_text(strip=True) + " "
                        elif content.get('class') == ['n']:
                            name += content.get_text(strip=True)
                        elif content.get('class') == ['bn']:
                            name += " " + content.get_text(strip=True)
                name = name.strip()
                name = re.sub(r'\s+', ' ', name)
                return name
            elif isinstance(pp, str):
                name += content.strip() + " "
                return name
            else:
                return ""
        
        name = trace_spanowanie(name_pp)
        latin_name = trace_spanowanie(latin_name_pp)
        
        if name == "" or latin_name == "":
            continue

        link = f"https://e-katalogroslin.pl{element.find('a')['href']}"
        
        img_link = element.find('img', class_='offer_pic')['src']  
        if img_link == no_image:
            img_link = default_img
            
        plant_info = {
            'name': name.lower(),
            'latin_name': latin_name.lower(),
            'link': link,
            'image': img_link
        }
        plant_info_list.append(plant_info)

    return plant_info_list

async def get_all_plant_info(start_url, amount=999):
    max_tries = 3
    page_number = 1
    all_plant_info = []
    current_url = start_url
    driver.get(start_url)
    
    try:
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "listning")))
    except:
        print("Timeout! Nie udało się załadować sekcji listingu.")
        return None

    async with aiohttp.ClientSession() as session:
        while current_url != last_url and amount != 0:
            plant_info_list = await parse_page(driver.page_source)
            tasks = [asyncio.create_task(get_plant_properties(session, plant['link'])) for plant in plant_info_list]
            properties_list = await asyncio.gather(*tasks)

            for plant, properties in zip(plant_info_list, properties_list):
                plant.update(properties)
                del plant['link']

            plant_info_list = [plant for plant in plant_info_list if plant.get('docelowa_wysokosc')]
            
            all_plant_info.extend(plant_info_list)
            
            if(page_number % 10 == 0):
                print(f"Strona [{page_number}]. Pobrano dane z: {current_url}")     
            
            for _ in range(max_tries):
                try:
                    next_button = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.XPATH, "//button[@data-next]")))
                    next_button.click()
                    WebDriverWait(driver, 10).until(EC.url_changes(current_url))
                    break
                except (ElementClickInterceptedException, StaleElementReferenceException, TimeoutException):
                    continue
            try:
                WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, "listning")))
            except:
                print("eeeeeooooo")
                
            new_url = driver.current_url
            if new_url == current_url:
                print("URL nie zmienił się. Spróbuj ponownie.")
                continue

            current_url = new_url
            page_number += 1
            amount -= 1

    return all_plant_info
    
    
input_page = "https://e-katalogroslin.pl/przegladaj-katalog"
output_name = "katalog_roslin.csv"

le_test = "https://e-katalogroslin.pl/plants/3950,malus-domestica"
before_last = "https://e-katalogroslin.pl/przegladaj-katalog?se=d306e4bd899cb4c4f423d4545941b3fd"
test = "https://e-katalogroslin.pl/przegladaj-katalog?se=38ec3b62fa9d7cb98b7e2a47becf8f04"
test_file = "testowy.csv"

def commit_scrap(page, output, amount=999):
    plant_info_list = asyncio.run(get_all_plant_info(page, amount))
    if plant_info_list:
        df = pd.DataFrame(plant_info_list)
        df = df.fillna('Brak')
        df.to_csv(output, index=False)
        print(f"Zapisano dane do plikuuu {output}.")
    else:
        print("ej co jest.")
    
    # Jeszcze raz bo grupa_roslin z jakiegos powodu miala pusta wartosc    
    df = pd.read_csv(output)
    df = df.fillna("Brak")
    df.to_csv(output, index=False)
    
def main():
    start_time = time.time()
    
    commit_scrap(input_page, test_file, 3)
    
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Czas trwania scrapowania: {elapsed_time} sekund")
    driver.quit()

if __name__ == "__main__":
    main()