export enum RoslinaRelacje {
  MA_FORME = 'MA_FORME',
  MA_GLEBE = 'MA_GLEBE',
  MA_GRUPE = 'MA_GRUPE',
  MA_KOLOR_LISCI = 'MA_KOLOR_LISCI',
  MA_KOLOR_KWIATOW = 'MA_KOLOR_KWIATOW',
  MA_KWIAT = 'MA_KWIAT',
  MA_NAGRODE = 'MA_NAGRODE',
  MA_ODCZYN = 'MA_ODCZYN',
  MA_OKRES_KWITNIENIA = 'MA_OKRES_KWITNIENIA',
  MA_OKRES_OWOCOWANIA = 'MA_OKRES_OWOCOWANIA',
  MA_OWOC = 'MA_OWOC',
  MA_PODGRUPE = 'MA_PODGRUPE',
  MA_POKROJ = 'MA_POKROJ',
  MA_SILE_WZROSTU = 'MA_SILE_WZROSTU',
  MA_STANOWISKO = 'MA_STANOWISKO',
  MA_WALOR = 'MA_WALOR',
  MA_WILGOTNOSC = 'MA_WILGOTNOSC',
  MA_ZASTOSOWANIE = 'MA_ZASTOSOWANIE',
  MA_ZIMOZIELONOSC_LISCI = 'MA_ZIMOZIELONOSC_LISCI'
}


const etykietaToRelacjaMap: { [key: string]: RoslinaRelacje } = {
  'forma': RoslinaRelacje.MA_FORME,
  'gleba': RoslinaRelacje.MA_GLEBE,
  'grupa': RoslinaRelacje.MA_GRUPE,
  'kolorlisci': RoslinaRelacje.MA_KOLOR_LISCI,
  'kolorkwiatow': RoslinaRelacje.MA_KOLOR_KWIATOW,
  'kwiat': RoslinaRelacje.MA_KWIAT,
  'nagroda': RoslinaRelacje.MA_NAGRODE,
  'odczyn': RoslinaRelacje.MA_ODCZYN,
  'okreskwitnienia': RoslinaRelacje.MA_OKRES_KWITNIENIA,
  'okresowocowania': RoslinaRelacje.MA_OKRES_OWOCOWANIA,
  'owoc': RoslinaRelacje.MA_OWOC,
  'podgrupa': RoslinaRelacje.MA_PODGRUPE,
  'pokroj': RoslinaRelacje.MA_POKROJ,
  'silawzrostu': RoslinaRelacje.MA_SILE_WZROSTU,
  'stanowisko': RoslinaRelacje.MA_STANOWISKO,
  'walor': RoslinaRelacje.MA_WALOR,
  'wilgotnosc': RoslinaRelacje.MA_WILGOTNOSC,
  'zastosowanie': RoslinaRelacje.MA_ZASTOSOWANIE,
  'zimozielonosc': RoslinaRelacje.MA_ZIMOZIELONOSC_LISCI
};

function normalizeString(str: string): string {
  return str
    .toLowerCase()
    .replace(/\s+/g, '')
    .replace(/[ąćęłńóśźż]/g, match => {
      const map: { [key: string]: string } = {
        'ą': 'a',
        'ć': 'c',
        'ę': 'e',
        'ł': 'l',
        'ń': 'n',
        'ó': 'o',
        'ś': 's',
        'ź': 'z',
        'ż': 'z'
      };
      return map[match];
    });
}

export function getRelacjaByEtykieta(etykieta: string): RoslinaRelacje | undefined {
  const normalizedEtykieta = normalizeString(etykieta);
  return etykietaToRelacjaMap[normalizedEtykieta];
}
