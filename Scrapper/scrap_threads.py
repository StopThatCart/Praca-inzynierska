import requests
import pandas as pd
import aiohttp
import asyncio
import re
from bs4 import BeautifulSoup
import time
from concurrent.futures import ThreadPoolExecutor

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import ElementClickInterceptedException, StaleElementReferenceException

input_page = "https://e-katalogroslin.pl/przegladaj-katalog"
output_name = "katalog_roslin.csv"
no_description = "Ta roślina nie posiada opisu"

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

async def fetch(session, url):
    async with session.get(url) as response:
        return await response.text()

def clean_description(description):
    description = description.strip().replace('"', "'").replace('\t', '\n').replace('\n', ' ')

    # usuwanie znaków specjalnych
    cleaned_description = re.sub(r'[^\w\s]', '', description)
    # usuwanie nadmiarowych spacji pomiedzy wierszami
    cleaned_description = re.sub(r'\s+', ' ', cleaned_description)
    return cleaned_description

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
            plant_properties[property_name] = property_value

    return plant_properties

def parse_page(html):
    soup = BeautifulSoup(html, 'html.parser')
    listing_section = soup.find('section', id='listning')
    elements = listing_section.find_all('div', class_='element')
    plant_info_list = []

    for element in elements:
        desc = element.find('div', class_='description').find('p', class_='desc_desc').get_text(strip=True)
        if desc == no_description:
            continue

        name = element.find('div', class_='description').find('p', class_='desc_title').get_text(strip=True)
        latin_name = element.find('div', class_='description').find('p', class_='desc_pl_title').get_text(strip=True)
        link = f"https://e-katalogroslin.pl{element.find('a')['href']}"

        plant_info = {
            'name': name.lower(),
            'latin_name': latin_name.lower(),
            'link': link
        }
        plant_info_list.append(plant_info)

    return plant_info_list

async def get_plant_info_for_url(session, start_url, end_url, stop_urls):
    
    print(f"Ej, pa tera: {start_url}")
    print(f"A tu końcowy: {end_url}\n")
    firefox_options = webdriver.FirefoxOptions()
    firefox_options.add_argument("--headless")
    driver = webdriver.Firefox(options=firefox_options)
    driver.implicitly_wait(10)
    
    max_tries = 3
    hehe = 1
    all_plant_info = []
    hundreds_pages = []
    current_url = start_url
    driver.get(start_url)

    try:
        WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, "listning")))
    except:
        print(f"Timeout! Nie udało się załadować sekcji listingu dla URL: {start_url}")
        driver.quit()
        return all_plant_info, hundreds_pages

    while current_url != end_url:
        current_url = driver.current_url
        
        if current_url in stop_urls:
            break
        
        if hehe % 10 == 0:
            hundreds_pages.append(current_url)

        plant_info_list = parse_page(driver.page_source)
        tasks = [asyncio.create_task(get_plant_properties(session, plant['link'])) for plant in plant_info_list]
        properties_list = await asyncio.gather(*tasks)

        for plant, properties in zip(plant_info_list, properties_list):
            plant.update(properties)
            del plant['link']

        all_plant_info.extend(plant_info_list)
        print(f"Strona [{hehe}]. Pobrano dane z: {driver.current_url}")

        for _ in range(max_tries):
            try:
                next_button = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((By.XPATH, "//button[@data-next]")))
                next_button.click()
                WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, "listning")))
                break
            except ElementClickInterceptedException:
                WebDriverWait(driver, 10).until_not(EC.presence_of_element_located((By.ID, "loader")))
            except StaleElementReferenceException:
                continue
        hehe += 1

    driver.quit()
    return all_plant_info

async def get_all_plant_info():
    async with aiohttp.ClientSession() as session:
        with ThreadPoolExecutor(max_workers=len(url_list)) as executor:
            loop = asyncio.get_event_loop()
            tasks = [
                loop.run_in_executor(
                    executor,
                    lambda url=url, stop_urls=[stop_url for stop_url in url_list if stop_url != url]: asyncio.run(
                        get_plant_info_for_url(
                            session, 
                            url,
                            last_url,
                            stop_urls
                        )
                    )
                )
                for url in url_list
            ]
            results = await asyncio.gather(*tasks)

    all_plant_info = []
    for result in results:
        all_plant_info.extend(result[0])

    return all_plant_info


def main():
    start_time = time.time()
    plant_info_list = asyncio.run(get_all_plant_info())
    if plant_info_list:
        df = pd.DataFrame(plant_info_list)
        df.to_csv(output_name, index=False)
        print(f"Zapisano dane do pliku {output_name}.")

    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Czas trwania scrapowania: {elapsed_time} sekund")

if __name__ == "__main__":
    main()
