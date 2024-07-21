import { Engine, Scene, DisplayMode, Vector, vec, IsometricMap, ImageSource } from "excalibur";
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
  tileWidth: 16,
  tileHeight: 8,
  columns: 2,
  rows: 2,
  renderFromTopOfGraphic: true
});

/*
//game.currentScene.add(isoMap);

*/
game.start(loader).then(() => {
  const sprite = Resources.Kot.toSprite();
  for (let tile of isoMap.tiles) {
    tile.addGraphic(sprite);
  }

  game.currentScene.add(isoMap); // Dodajemy isoMap do sceny po załadowaniu zasobów
  calculateExPixelConversion(game.screen);
});