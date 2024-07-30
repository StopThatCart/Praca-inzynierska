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
            res.append([None, None])
        else:
            res.append([None, None])
    return res
     
def height_query_string_to_class(min_height, max_height):
    d_name = None
    d_min = None
    d_max = None

    if min_height == "Brak" or max_height == "Brak":
        d_name = "Brak"
    if min_height is not None and max_height is not None:
        d_min = min_height
        d_max = max_height
    elif min_height is not None:
        d_min = min_height
    elif max_height is not None:
        d_max = max_height
    else:
        d_name = "Brak"
    
    height_dict = {
        "name": d_name,
        "min": d_min,
        "max": d_max
    }
    
    height_params = None
    
    if height_dict['name'] is None:
        height_dict['name'] = "cokolwiek innego"
    if height_dict['min'] is None:
        height_dict['min'] = 0
    if height_dict['max'] is None:
        height_dict['max'] = height_dict['min']
     
    height_params = {k: v for k, v in height_dict.items() if v is not None}
    
    return height_params
       
def main(heights):
    ret = []
    heights = heights.split(',')
    res = process_height_string(heights)
    for r in res:
      ret.append(height_query_string_to_class(r[0], r[1]))
    return ret