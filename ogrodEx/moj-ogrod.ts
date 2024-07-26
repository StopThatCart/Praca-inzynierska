import { Engine, Scene, DisplayMode, Vector, vec, IsometricMap, ImageSource, Shape } from "excalibur";
import { calculateExPixelConversion } from "./ui.js";
import { loader, Resources } from "./resources.js";
///import { isometricLoader } from "./isometricLoader.js";

const game = new Engine({
  canvasElementId: 'my-canvas-id',
  width: 800,
  height: 600,
  pixelArt: true,
  displayMode: DisplayMode.FitContainerAndFill
});

game.screen.events.on('resize', () => calculateExPixelConversion(game.screen));



const isoMap = new IsometricMap({
  pos: vec(250, 10),
  tileWidth: 64,
  tileHeight: 64,
  columns: 6,
  rows: 6,
  renderFromTopOfGraphic: true
});

game.start(loader).then(() => {
  const sprite = Resources.Kot.toSprite();
  sprite.width = isoMap.tileWidth;
  sprite.height = isoMap.tileHeight;

  for (let tile of isoMap.tiles) {
    tile.solid = true;
    tile.addCollider(Shape.Polygon([vec(0, 95), vec(55, -32 + 95), vec(111, 95), vec(55, 32 + 95)]));
   // tile.addGraphic(sprite.clone());
  }

  game.currentScene.add(isoMap);
  calculateExPixelConversion(game.screen);
});