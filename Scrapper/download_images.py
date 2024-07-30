import pandas as pd
import requests
import os
from io import BytesIO
from PIL import Image
import re


default_img = "default_plant.jpg"

def sanitize_filename(name):
    # Usuń specjalne znaki i zastąp je znakiem podkreślenia
    sanitized_name = re.sub(r'[^a-zA-Z0-9\s]', '', name)
    # Usuń pojedyncze apostrofy (') z wyjątkiem tych wewnątrz słów
    sanitized_name = re.sub(r"\b'\b", '', sanitized_name)
    # Usuń nadmiarowe spacje
    sanitized_name = re.sub(r'\s+', ' ', sanitized_name)
    # Usuń spacje na początku i końcu nazwy
    sanitized_name = sanitized_name.strip()
    # Zamień spacje na podkreślenia
    sanitized_name = re.sub(r'\s', '_', sanitized_name)
    return sanitized_name


def get_images(file_path, image_link, name_label):
    df = pd.read_csv(file_path)
    
    if image_link not in df.columns or name_label not in df.columns:
        raise ValueError(f"Kolumny '{image_link}' lub '{name_label}' nie istnieją w pliku CSV.")
    
    if not os.path.exists('images'):
        os.makedirs('images')
    
    image_names = []
    
    count = 1
    for index, row in df.iterrows():
        url = row[image_link]
        name = row[name_label]
        
        if url == default_img:
            img_filename = default_img
            image_names.append(img_filename)
        else:
            try:
                response = requests.get(url)
                response.raise_for_status()
                
                img = Image.open(BytesIO(response.content))
                sanitized_name = sanitize_filename(name)
                
                img_path = os.path.join('images', f"{sanitized_name}.jpg")
                img.save(img_path)
                
                img_filename = f"{sanitized_name}.jpg"
                image_names.append(img_filename)
            except requests.RequestException as e:
                print(f"Błąd podczas pobierania obrazu z {url}: {e}")
                img_filename = default_img
                image_names.append(img_filename)
        if count % 25 == 0:
            print(f"Aktualny rząd: [{count}]")
        count = count + 1
    
    df['image_filename'] = image_names
    df.to_csv(file_path, index=False)
    
    print("Zakończono działanie programu.")

# Przykładowe wywołanie funkcji
file_path = 'katalog_roslin.csv'
image_link = 'image'
name_label = 'latin_name'

get_images(file_path, image_link, name_label)
