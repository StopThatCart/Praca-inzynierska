from neo4j import GraphDatabase
import csv
import pandas as pd
import time
from process_heights import main as process_height

# Parametry do połączenia z bazą danych Neo4j
neo4j_uri = "bolt://localhost:7687"
neo4j_username = "neo4j"
neo4j_password = "11111111"

csv_filename = "katalog_roslin.csv"

def query_string(node_name, label, relationship):
    query = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Roslina {{name: plant.name, latin_name: plant.latin_name}}) "
        "WITH p, plant "
        f"UNWIND plant.{label} AS item "
        f"MERGE (n:{node_name.capitalize()} {{name: item}}) "
        f"MERGE (p)-[:{relationship}]->(n) "
        f"MERGE (n)-[:zawiera_rosline]->(p)"
    )
    return query
        
queries = [
    ("Grupa", "groups", "nalezy_do_grupy"),
    ("Podgrupa", "subgroups", "nalezy_do_podgrupy"),
    ("Forma", "forms", "ma_forme"),
    ("Sila_wzrostu", "growth_strength", "ma_sile_wzrostu"),
    ("Pokroj", "shapes", "ma_pokroj"),
    # TODO: WYSOKOŚĆ DO ZMIANY
    #("Wysokosc", "heights", "ma_wysokosc"),
    ("Kolor", "leaves_colors", "ma_liscie_koloru"),
    ("Zimozielonosc_lisci", "wintergreen_leaves", "ma_zimozielonosc_lisci"),
    ("Owoc", "fruits", "ma_owoc"),
    ("Stanowisko", "positions", "ma_stanowisko"),
    ("Wilgotnosc", "humidities", "ma_wilgotnosc"),
    ("Odczyn", "phs", "ma_odczyn_gleby"),
    ("Gleba", "soils", "ma_glebe"),
    ("Walor", "appeals", "ma_walor"),
    ("Zastosowanie", "uses", "ma_zastosowanie"),
    ("Nagroda", "awards", "ma_nagrode"),
    ("Kwiat", "flowers", "ma_kwiat"),
    ("Kolor", "flower_colors", "ma_kwiat_koloru"),
    ("Okres", "flowering_periods", "ma_okres_kwitnienia"),
    ("Okres", "fruiting_times", "ma_okres_owocowania")
]

def import_plants(csv_filename, uri, username, password):
    driver = GraphDatabase.driver(uri, auth=(username, password))
    
    with open(csv_filename, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        
        plant_query_plant = (
        "UNWIND $plants AS plant "
        "MERGE (p:Roslina {name: plant.name, latin_name: plant.latin_name, description: plant.description}) "
        )
        
        plants_data = []
        for row in reader:
            plant_data = {
                'name': row['name'],
                'latin_name': row['latin_name'],
                'description': row['opis'],
                'groups': row['grupa_roslin'].split(', '),
                'subgroups': row['grupa_uzytkowa'].split(', '),
                'forms': row['forma'].split(', '),
                'growth_strength': row['sila_wzrostu'],
                'shapes': row['pokroj'].split(', '),
                'heights': process_height(row['docelowa_wysokosc']),
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

    with driver.session() as session:
        print("Dodawanie roślin...")
        for batch_start in range(0, len(plants_data), 100):
            batch_end = batch_start + 100
            batch = plants_data[batch_start:batch_end]
            session.run(plant_query_plant, plants=batch)
        
        # Generowanie zapytań
        for node, label, relationship in queries:
            q = query_string(node, label, relationship)
            print(f"Dodawanie węzłów {node.capitalize()}...")
            for batch_start in range(0, len(plants_data), 100):
                batch_end = batch_start + 100
                batch = plants_data[batch_start:batch_end]
                session.run(q, plants=batch)
         
        print(f"Dodawanie wysokości...")
        
        # Dodawanie wysokości
        for plant in plants_data:
            heights = plant['heights']
            for height in heights:
                session.run(
                    f"UNWIND $plants AS plant "
                    f"MATCH (p:Roslina {{name: plant.name, latin_name: plant.latin_name}}) "
                    f"MERGE {height} "
                    f"MERGE (p)-[:ma_wysokosc]->(w) "
                    f"MERGE (w)-[:zawiera_rosline]->(p)",
                    plants=[plant]
                )

    print("Zakończono dodawanie roślin")
    driver.close()


start_time = time.time()

import_plants(csv_filename, neo4j_uri, neo4j_username, neo4j_password)


end_time = time.time()
elapsed_time = end_time - start_time
print(f"Czas trwania importowania: {elapsed_time} sekund")