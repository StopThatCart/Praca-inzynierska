import { TiledResource } from '@excaliburjs/plugin-tiled';
import { Engine, Scene, TileMap, Color, vec } from 'excalibur';
import { Loader } from 'excalibur';

import { Resources, loader } from './resources.js';



// Utwórz nowy silnik Excalibur
const game = new Engine({
  width: 800,
  height: 600,
  backgroundColor: Color.Black
});

game.start(loader).then(() => {
  Resources.TiledMap.addToScene(game.currentScene);
});