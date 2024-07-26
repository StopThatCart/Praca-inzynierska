
import { DefaultLoader, Engine, FontSource, ImageSource, Loader, SpriteSheet } from 'excalibur';
import KotImage from './resources/kot.png';
import { TiledResource } from '@excaliburjs/plugin-tiled';


export const Resources = {
  Kot: new ImageSource(KotImage),
  TiledMap: new TiledResource('./resources/first-level.tmx', {
    entityClassNameFactories: {
      //  player: (props) => {
      //      const player = new Player(props.worldPos);
      //      player.z = 100;
      //      return player;
      //  }
    },
  })
} as const;



export const loader = new Loader();
for (let resource of Object.values(Resources)) {
    loader.addResource(resource);
}
