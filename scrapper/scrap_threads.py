import requests
import pandas as pd
import aiohttp
import asyncio
from bs4 import BeautifulSoup
import time
import csv
import re
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import ElementClickInterceptedException, StaleElementReferenceException, TimeoutException

# konfig przeglądarki
firefox_options = webdriver.FirefoxOptions()
firefox_options.add_argument("--headless")

banned_properties = ['pochodzenie', 'zasieg_geograficzny', 'strefa', 'nagrody']

url_list = [
    "https://e-katalogroslin.pl/przegladaj-katalog?se=b873c55f322d2ae7c077b52480a75f81",
    
    
##    "https://e-katalogroslin.pl/przegladaj-katalog?se=6fa1929f87113a75900ba8d4b4b46a98",

    # "https://e-katalogroslin.pl/przegladaj-katalog?se=39921be9413de6ac538a4df45da7d104",
    
   "https://e-katalogroslin.pl/przegladaj-katalog?se=38ec3b62fa9d7cb98b7e2a47becf8f04",

    # "https://e-katalogroslin.pl/przegladaj-katalog?se=0f53540eb1787eab5cf47d55874e4372",
    
##    "https://e-katalogroslin.pl/przegladaj-katalog?se=5dc50211f4e63ccd8eefa37c99d6186b",

    "https://e-katalogroslin.pl/przegladaj-katalog?se=04c2fcb871ee0704e7af0cd6183a01e1",
    
##    "https://e-katalogroslin.pl/przegladaj-katalog?se=5869b59c0dfb79d0604ad654ddf4b404",

    # "https://e-katalogroslin.pl/przegladaj-katalog?se=85f76f2e4609984bd0caa1a567285df3",
    # "https://e-katalogroslin.pl/przegladaj-katalog?se=b0282224a707a5010c36fa11e8fd8c18",
    "https://e-katalogroslin.pl/przegladaj-katalog?se=e2d860561d7ad162458f2301c4b89bea"
    # "https://e-katalogroslin.pl/przegladaj-katalog?se=c94bf7912b270f9c04423786d26f051f"
]

first_url = "https://e-katalogroslin.pl/przegladaj-katalog?se=b873c55f322d2ae7c077b52480a75f81"
last_url = "https://e-katalogroslin.pl/przegladaj-katalog?se=4d4d14322be96da421727124e8e76fff"

no_description = "Ta roślina nie posiada opisu."
no_image = "https://e-katalogroslin.pl/wp-content/themes/e-katalogroslin/img/e-kat_noimg.jpg"
default_img = "default_plant.jpg"

null_placeholder = "Brak"

def remove_polish(str):
    return str.lower().replace('ł', 'l').replace('ą', 'a').replace('ć', 'c').replace('ę', 'e').replace('ś', 's').replace('ń', 'n').replace('ó', 'o').replace('ż', 'z').replace('ź', 'z')

def clean_description(description):
    description = description.strip().replace('"', "'").replace('\t', '\n')

    # usuwanie znaków specjalnych
   # description = re.sub(r'[^\w\s]', '', description)
    # usuwanie nadmiarowych spacji pomiedzy wierszami
    cleaned_description = re.sub(r'\s+', ' ', description)
    return cleaned_description

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.text()

async def get_plant_properties(session, plant_url):
    html = await fetch(session, plant_url)
    soup = BeautifulSoup(html, 'html.parser')
    table_rows = soup.select('table.product_details_table tr')
    plant_properties = {}
    desc = soup.find('p', {"class": "description"})
    if desc is not None:
        desc = clean_description(desc.get_text())
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
        
        name_span = element.find('div', class_='description').find('p', class_='desc_pl_title').find("span")
        latin_name_span = element.find('div', class_='description').find('p', class_='desc_title')

        def process_span(span_elem):
            if span_elem:
                name = ""
                for content in span_elem.contents:
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
            elif isinstance(span_elem, str):
                name += content.strip() + " "
                return name
            else:
                return ""
        
        name = process_span(name_span)
        latin_name = process_span(latin_name_span)
        
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


async def get_all_plant_info(start_url, url_list, amount=9999):
    max_tries = 5
    page_number = 1
    all_plant_info = []
    current_url = start_url
    driver = webdriver.Firefox(options=firefox_options)
    driver.implicitly_wait(15)
    driver.get(start_url)
    thread_id = threading.get_ident()
    tries = 0 
    
    try:
        WebDriverWait(driver, 15).until(EC.presence_of_element_located((By.ID, "listning")))
    except:
        print(f"Timeout! Nie udało się załadować sekcji listingu dla {start_url}.")
        driver.quit()
        return None

    async with aiohttp.ClientSession() as session:
        while amount != 0:
            if start_url != first_url and current_url == first_url:
                print(f"Zatrzymanie wątku, napotkano pierwszą stronę: {current_url}")
                break
            if current_url in url_list and current_url != start_url:
                print(f"Zatrzymanie wątku, napotkano adres startowy innego wątku: {current_url}")
                break
            
            plant_info_list = await parse_page(driver.page_source)
            tasks = [asyncio.create_task(get_plant_properties(session, plant['link'])) for plant in plant_info_list]
            properties_list = await asyncio.gather(*tasks)

            updated_plant_info_list = []
            for plant, properties in zip(plant_info_list, properties_list):
                if  properties.get('opis') is None:
                    continue
                plant.update(properties)
                del plant['link']
                updated_plant_info_list.append(plant)

            plant_info_list = [plant for plant in updated_plant_info_list if plant.get('docelowa_wysokosc')]
            
            all_plant_info.extend(plant_info_list)
            
            if current_url == last_url:
                print(f"Wątek [{thread_id}]: Osiągnięto ostatnią stronę: {current_url}")
                break
            
            if(page_number % 10 == 0):
                print(f"Wątek [{thread_id}]: Przetworzone strony: [{page_number}]. Pobrano dane z: {current_url}")     
            
            for _ in range(max_tries):
                try:
                    next_button = WebDriverWait(driver, 15).until(EC.element_to_be_clickable((By.XPATH, "//button[@data-next]")))
                    next_button.click()
                    WebDriverWait(driver, 15).until(EC.url_changes(current_url))
                    break
                except (ElementClickInterceptedException, StaleElementReferenceException, TimeoutException):
                    continue
            try:
                WebDriverWait(driver, 7).until(EC.presence_of_element_located((By.ID, "listning")))
            except:
                print("Nie udało się załadować nowej strony.")
                
            new_url = driver.current_url
            if new_url == current_url:
                print(f"Wątek [{thread_id}]: URL nie zmienił się. Spróbuj ponownie: [{tries}].")
                tries += 1
                if tries >= 10:
                    print("Przekroczono limit prób zmiany adresu.")
                    try:
                        next_button = WebDriverWait(driver, 15).until(EC.element_to_be_clickable((By.XPATH, "//button[@data-next]")))
                        next_button.click()
                        WebDriverWait(driver, 15).until(EC.url_changes(current_url))
                        break
                    except (ElementClickInterceptedException, StaleElementReferenceException, TimeoutException):
                        continue
                continue

            current_url = new_url
            page_number += 1
            amount -= 1
    print(f"Wątek [{thread_id}]: Zakończono scrapowanie dla adresu: {start_url}\n Ostatnia strona: {current_url}")
    driver.quit()
    return all_plant_info

def worker(url, url_list):
    return asyncio.run(get_all_plant_info(url, url_list))

def commit_scrap(urls, output, amount=999):
    plant_info_list = []
    with ThreadPoolExecutor(max_workers=len(urls)) as executor:
        futures = {executor.submit(worker, url, urls): url for url in urls}
        for future in as_completed(futures):
            result = future.result()
            if result:
                plant_info_list.extend(result)
    
    if plant_info_list:
        df = pd.DataFrame(plant_info_list)
        df = df.fillna(null_placeholder)
        df.to_csv(output, index=False)
        print(f"Zapisano dane do pliku {output}.")
    else:
        print("Brak danych do zapisania.")
    
    # Jeszcze raz bo grupa_roslin z jakiegos powodu miala pusta wartosc    
    df = pd.read_csv(output)
    df = df.fillna(null_placeholder)
    df.to_csv(output, index=False)


output_name = "katalog_roslin2.csv"
test_file = "testowy.csv"

def main():
    start_time = time.time()
    
    print("Rozpoczęcie scrapowania...")
    commit_scrap(url_list, output_name)
    
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Czas trwania scrapowania: {elapsed_time} sekund")

if __name__ == "__main__":
    main()
