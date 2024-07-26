import * as ex from 'excalibur';

// Tworzenie gry
const game = new ex.Engine({
  width: 800,
  height: 600
});

// Konfiguracja rozmiarów kafelków
const tileWidth = 64;
const tileHeight = 32;

// Klasa dla izometrycznej mapy
class IsometricMap {
  public tiles: ex.Actor[][] = [];
  
  constructor(public rows: number, public cols: number) {
    this.initializeTiles();
  }

  private initializeTiles() {
    for (let row = 0; row < this.rows; row++) {
      this.tiles[row] = [];
      for (let col = 0; col < this.cols; col++) {
        const x = (col - row) * (tileWidth / 2);
        const y = (col + row) * (tileHeight / 2);
        const tile = new ex.Actor({
          pos: new ex.Vector(400 + x, 300 + y),
          width: tileWidth,
          height: tileHeight,
          color: ex.Color.fromRGB(200, 200, 200)
        });
        this.tiles[row][col] = tile;
        game.add(tile);
      }
    }
  }
}

// Tworzenie izometrycznej mapy 10x10
const isoMap = new IsometricMap(10, 10);

// Tworzenie obiektów na mapie
const tree = new ex.Actor({
  pos: new ex.Vector(400, 300),
  width: 32,
  height: 64,
  color: ex.Color.Green
});

const rock = new ex.Actor({
  pos: new ex.Vector(400 + (1 - 2) * (tileWidth / 2), 300 + (1 + 2) * (tileHeight / 2)),
  width: 32,
  height: 32,
  color: ex.Color.Gray
});

game.add(tree);
game.add(rock);

// Start gry
game.start();
