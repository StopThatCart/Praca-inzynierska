import csv
import re

def get_estimated_heights(file_path):
    estimated_heights = []
    with open(file_path, mode='r', encoding='utf-8') as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            estimated_height = row['docelowa_wysokosc']
            if estimated_height:
                estimated_heights.append(estimated_height)
            else:
                estimated_heights.append("Brak")
    return estimated_heights

def convert_commas_to_dots(value):
    # Zastosowanie wyrażenia regularnego do zamiany przecinków na kropki
    return re.sub(r'(\d+,\d+)', lambda match: match.group(0).replace(',', '.'), value)

def process_height_string(heights):
    res = []
    for height in heights:
        height = height.strip()
        if "powyżej" in height:
           res.append([height.split("powyżej")[1].strip(), None])
        elif "od" in height and "do" in height:
            min_height, max_height = height.split("od")[1].split("do")
            res.append([min_height.replace('m', '').strip(), max_height.replace('m', '').strip()])
        elif "do" in height:
             res.append([None, height.split("do")[1].replace('m', '').strip()])
        elif "od" in height:
            res.append([height.split("od")[1].replace('m', '').strip(), None])
        elif "Brak":
            res.append(["Brak", "Brak"])
        else:
            res.append([None, None])
    return res

def height_query_string(min_height, max_height):
    if min_height == "Brak" or max_height == "Brak":
        return f"(:Wysokosc{{name: Brak}})"
    if min_height is not None and max_height is not None:
        return f"(:Wysokosc{{min: {min_height}, max: {max_height}}})"
    elif min_height is not None:
        return f"(:Wysokosc{{min: {min_height}}})"
    elif max_height is not None:
        return f"(:Wysokosc{{max: {max_height}}})"
    else:
        return f"(:Wysokosc{{name: Brak}})"
    
def height_query_string2(min_height, max_height):
    if min_height == "Brak" or max_height == "Brak":
        bep = f"(w:Wysokosc{{name: Brak}})"
    if min_height is not None and max_height is not None:
        bep = f"(w:Wysokosc{{min: {min_height}, max: {max_height}}})"
    elif min_height is not None:
        bep = f"(w:Wysokosc{{min: {min_height}}})"
    elif max_height is not None:
        bep = f"(w:Wysokosc{{max: {max_height}}})"
    else:
        bep = f"(w:Wysokosc{{name: Brak}})"
    
    
    query_no_neo4j = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Roslina {{name: plant.name, latin_name: plant.latin_name}}) "
        "WITH p, plant "
        #f"UNWIND plant.heights AS item "
        #f"MERGE (n:Wysokosc {{name: item}}) "
        f"MERGE {bep} "
        f"MERGE (p)-[:ma_wysokosc]->(w) "
        f"MERGE (w)-[:zawiera_rosline]->(p)"
    )
    
    query = (
        "UNWIND $plants AS plant "
        f"MATCH (p:Roslina {{name: plant.name, latin_name: plant.latin_name}}) "
        "WITH p, plant "
        f"UNWIND plant.heights AS item "
        #f"MERGE (n:Wysokosc {{name: item}}) "
        f"MERGE item "
        f"MERGE (p)-[:ma_wysokosc]->(w) "
        f"MERGE (w)-[:zawiera_rosline]->(p)"
    )
    return bep
            
def main(heights):
    ret = []
    heights = heights.split(',')
    res = process_height_string(heights)
    for r in res:
      ret.append(height_query_string2(r[0], r[1]))
    return ret