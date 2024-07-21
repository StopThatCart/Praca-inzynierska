
import { DefaultLoader, Engine, FontSource, ImageSource, Loader, SpriteSheet } from 'excalibur';
import KotImage from './resources/kot.png';


export const Resources = {
  Kot: new ImageSource(KotImage)
} as const;



export const loader = new Loader(Object.values(Resources));
