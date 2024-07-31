from neo4j import GraphDatabase
import csv
import pandas as pd
import time
from process_heights import main as process_height

# Parametry do połączenia z bazą danych Neo4j
database_name = "test" 
neo4j_uri = "bolt://localhost:7687"
neo4j_username = "neo4j"
neo4j_password = "11111111"


csv_filename = "katalog_roslin.csv"

brak = "Brak"

def query_string(node_name, label, relationship):
    query = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Roslina {{nazwa: plant.name, nazwa_lacinska: plant.latin_name}}) "
        "WITH p, plant "
        f"UNWIND plant.{label} AS item "
        f"WITH p, item WHERE item <> '{brak}' "
        f"MERGE (n:Wlasciwosc:{node_name.capitalize()} {{nazwa: item}}) "
        f"MERGE (p)-[:{relationship}]->(n) "
        f"MERGE (n)-[:ma_rosline]->(p)"
    )
    return query


def query_string_good(node_name, label, relationship):
    query = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Roslina {{nazwa: plant.name, nazwa_lacinska: plant.latin_name}}) "
        "WITH p, plant "
        f"UNWIND plant.{label} AS item "
        f"WITH p, item WHERE item <> '{brak}' "
        f"MERGE (n:Wlasciwosc:{node_name.capitalize()} {{nazwa: item}}) "
        f"MERGE (p)-[:{relationship}]->(n) "
        f"MERGE (n)-[:ma_rosline]->(p)"
    )
    return query

def query_string_en(node_name, label, relationship):
    query = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Plant {{name: plant.name, latin_name: plant.latin_name}}) "
        "WITH p, plant "
        f"UNWIND plant.{label} AS item "
        f"WITH p, item WHERE item <> '{brak}' "
        f"MERGE (n:Wlasciwosc:{node_name.capitalize()} {{name: item}}) "
        f"MERGE (p)-[:{relationship}]->(n) "
        f"MERGE (n)-[:has_plant]->(p)"
    )
    return query

plant_query = (
        "UNWIND $plants AS plant "
        "MERGE (p:Roslina {nazwa: plant.name, nazwa_lacinska: plant.latin_name, opis: plant.description, obraz: plant.image_filename}) "
        "WITH p, plant "
        "UNWIND plant.heights AS height "
        "WITH p, height "
        "WHERE height.name <> 'Brak' "
        "SET p.wysokosc_min = toFloat(height.min), p.wysokosc_max = toFloat(height.max)"
        )

plant_query_en = (
        "UNWIND $plants AS plant "
        "MERGE (p:Plant {name: plant.name, latin_name: plant.latin_name, description: plant.description, image: plant.image_filename}) "
        "WITH p, plant "
        "UNWIND plant.heights AS height "
        "WITH p, height "
        "WHERE height.name <> 'Brak' "
        "SET p.height_min = height.min, p.height_max = height.max"
        )
   
queries = [
    ("Grupa", "groups", "ma_grupe"),
    ("Podgrupa", "subgroups", "ma_podgrupe"),
    ("Forma", "forms", "ma_forme"),
    ("SilaWzrostu", "growth_strength", "ma_sile_wzrostu"),
    ("Pokroj", "shapes", "ma_pokroj"),
    ("Kolor", "leaves_colors", "ma_kolor_lisci"),
    ("Kolor", "flower_colors", "ma_kolor_wiatow"),
    ("Zimozielonosc", "wintergreen_leaves", "ma_zimozielonosc_lisci"),
    ("Owoc", "fruits", "ma_owoc"),
    ("Stanowisko", "positions", "ma_stanowisko"),
    ("Wilgotnosc", "humidities", "ma_wilgotnosc"),
    ("Odczyn", "phs", "ma_odczyn"),
    ("Gleba", "soils", "ma_glebe"),
    ("Walor", "appeals", "ma_walor"),
    ("Zastosowanie", "uses", "ma_zastosowanie"),
    ("Nagroda", "awards", "ma_nagrode"),
    ("Kwiat", "flowers", "ma_kwiat"),
    ("Okres", "flowering_periods", "ma_okres_kwitnienia"),
    ("Okres", "fruiting_times", "ma_okres_owocowania")
]   
        
queries_en = [
    ("Group", "groups", "has_group"),
    ("Subgroup", "subgroups", "has_subgroup"),
    ("Form", "forms", "has_form"),
    ("Growth_strength", "growth_strength", "has_growth_strength"),
    ("Shape", "shapes", "has_shape"),
    ("Color", "leaves_colors", "has_leaves_color"),
    ("Wintergreen_leaves", "wintergreen_leaves", "has_wintergreen_leaves"),
    ("Fruit", "fruits", "has_fruit"),
    ("Position", "positions", "has_position"),
    ("Humidity", "humidities", "has_humidity"),
    ("Ph", "phs", "has_ph"),
    ("Soil", "soils", "has_soil"),
    ("Appeal", "appeals", "has_appeal"),
    ("Use", "uses", "has_use"),
    ("Award", "awards", "has_award"),
    ("Flower", "flowers", "has_flower"),
    ("Color", "flower_colors", "has_flower_color"),
    ("Period", "flowering_periods", "has_flowering_period"),
    ("Period", "fruiting_times", "has_fruiting_period")
]

queries_test = [
    ("Group", "groups", "has_group"),
    ("Subgroup", "subgroups", "has_subgroup")
]

def import_plants(csv_filename, batch_size, uri, username, password):
    print("Rozpoczynanie seedowania bazy danych...")
    driver = GraphDatabase.driver(uri, auth=(username, password))
    
    with open(csv_filename, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        
        plants_data = []
        for row in reader:
            pain = process_height(row['docelowa_wysokosc'])
            plant_data = {
                'name': row['name'],
                'latin_name': row['latin_name'],
                'description': row['opis'],
                'image_name': row['image'],
                'groups': row['grupa_roslin'].split(', '),
                'subgroups': row['grupa_uzytkowa'].split(', '),
                'forms': row['forma'].split(', '),
                'growth_strength': row['sila_wzrostu'],
                'shapes': row['pokroj'].split(', '),
                'heights': pain,
                'leaves_colors': row['barwa_lisci_(igiel)'].split(', '),
                'wintergreen_leaves': row['zimozielonosc_lisci_(igiel)'].split(', '),
                'fruits': row['owoce'].split(', '),
                'positions': row['naslonecznienie'],
                'humidities': row['wilgotnosc'].split(', '),
                'phs': row['ph_podloza'].split(', '),
                'soils': row['rodzaj_gleby'].split(', '),
                'appeals': row['walory'].split(', '),
                'uses': row['zastosowanie'].split(', '),
                'awards': row['nagrody'].split(', '),
                'flowers': row['rodzaj_kwiatow'].split(', '),
                'flower_colors': row['barwa_kwiatow'].split(', '),
                'flowering_periods': row['pora_kwitnienia'].split(', '),
                'fruiting_times': row['pora_owocowania'].split(', ')
            }
            plants_data.append(plant_data)

    with driver.session(database=database_name) as session:
        print("Dodawanie roślin...")
        for batch_start in range(0, len(plants_data), batch_size):
            batch_end = min(batch_start + batch_size, len(plants_data))
            batch = plants_data[batch_start:batch_end]
            session.run(plant_query, plants=batch)
        
        for node, label, relationship in queries:
            q = query_string(node, label, relationship)
            print(f"Dodawanie węzłów {node.capitalize()}...")
            for batch_start in range(0, len(plants_data), batch_size):
                batch_end = min(batch_start + batch_size, len(plants_data))
                batch = plants_data[batch_start:batch_end]
                session.run(q, plants=batch)

    print("Zakończono dodawanie roślin")
    driver.close()

def seciczek(plants_data): 
    secik = set()
    for pl in plants_data:
        for h in pl['heights']:
            height_props = ", ".join([f"{key}: '{value}'" for key, value in h.items()])
            secik.add(height_props)        
    for s in secik:
          print(s)

def drop_database(uri, username, password):
    print("Czyszczenie bazy danych...")
    driver = GraphDatabase.driver(uri, auth=(username, password))
    with driver.session(database="system") as session:
        drop_query = (f"DROP DATABASE {database_name} IF EXISTS;")
        create_query = (f"CREATE DATABASE {database_name} IF NOT EXISTS;")
        
        session.run(drop_query)
        session.run(create_query)
    print("Zakończono czyszczenie bazy danych.")

start_time = time.time()

drop_database(neo4j_uri, neo4j_username, neo4j_password)
import_plants(csv_filename, 1000, neo4j_uri, neo4j_username, neo4j_password)


end_time = time.time()
elapsed_time = end_time - start_time
print(f"Czas trwania importowania: {elapsed_time} sekund")