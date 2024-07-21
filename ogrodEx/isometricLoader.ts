import * as ex from "excalibur";

const isoMap = new ex.IsometricMap({
    pos: ex.vec(250, 10),
    tileWidth: 32,
    tileHeight: 16,
    columns: 15,
    rows: 15,
    renderFromTopOfGraphic: true
  });
  
const image = new ex.ImageSource('./resources/kot.png');
await image.load();
  
const sprite = image.toSprite();
for (let tile of isoMap.tiles) {
    tile.addGraphic(sprite);
}