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

brak = "Brak"

def query_string(node_name, label, relationship):
    query = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Roslina {{name: plant.name, latin_name: plant.latin_name}}) "
        "WITH p, plant "
        f"UNWIND plant.{label} AS item "
        f"WITH p, item WHERE item <> '{brak}' "
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

queries_test = [
    ("Grupa", "groups", "nalezy_do_grupy"),
    ("Podgrupa", "subgroups", "nalezy_do_podgrupy")
]


def import_plants(csv_filename, batch_size, uri, username, password):
    print("Rozpoczynanie seedowania bazy danych...")
    driver = GraphDatabase.driver(uri, auth=(username, password))
    
    with open(csv_filename, newline='', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        
        plant_query_plant = (
        "UNWIND $plants AS plant "
        "MERGE (p:Roslina {name: plant.name, latin_name: plant.latin_name, description: plant.description}) "
        )
        
        plants_data = []
        for row in reader:
            pain = process_height(row['docelowa_wysokosc'])
            plant_data = {
                'name': row['name'],
                'latin_name': row['latin_name'],
                'description': row['opis'],
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

    with driver.session() as session:
        print("Dodawanie roślin...")
        for batch_start in range(0, len(plants_data), batch_size):
            batch_end = min(batch_start + batch_size, len(plants_data))
            batch = plants_data[batch_start:batch_end]
            session.run(plant_query_plant, plants=batch)
        
        for node, label, relationship in queries:
            q = query_string(node, label, relationship)
            print(f"Dodawanie węzłów {node.capitalize()}...")
            for batch_start in range(0, len(plants_data), batch_size):
                batch_end = min(batch_start + batch_size, len(plants_data))
                batch = plants_data[batch_start:batch_end]
                session.run(q, plants=batch)
        
        query_height = ("""
            UNWIND $plants AS plant 
            MATCH (p:Roslina {name: plant.name, latin_name: plant.latin_name}) 
            WITH p, plant 
            UNWIND plant.heights AS height 
            WITH p, height WHERE height.name <> 'Brak' 
            
            MERGE (n:Wysokosc {min: height.min, max: height.max}) 
            MERGE (p)-[:ma_wysokosc]->(n)
            MERGE (n)-[:zawiera_rosline]->(p)
        """
        )
        
        print(f"Dodawanie wysokości..")
        for batch_start in range(0, len(plants_data), batch_size):
            batch_end = min(batch_start + batch_size, len(plants_data))
            batch = plants_data[batch_start:batch_end]
            session.run(query_height, plants=batch)

        


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


# Bardzo wolno działa
def drop_database(uri, username, password):
    print("Czyszczenie bazy danych...")
    driver = GraphDatabase.driver(uri, auth=(username, password))
    with driver.session() as session:
        drop_query = (
        "MATCH (s) "
        "CALL apoc.nodes.delete(s, 1000) YIELD value "
        "RETURN value"
        )
        
        session.run(drop_query)
    print("Zakończono czyszczenie bazy danych.")

start_time = time.time()

#drop_database(neo4j_uri, neo4j_username, neo4j_password)
import_plants(csv_filename, 1000, neo4j_uri, neo4j_username, neo4j_password)


end_time = time.time()
elapsed_time = end_time - start_time
print(f"Czas trwania importowania: {elapsed_time} sekund")