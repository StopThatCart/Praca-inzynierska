/* tslint:disable */
/* eslint-disable */
import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';

import { RoslinaWlasnaService } from './services/roslina-wlasna.service';
import { RozmowaPrywatnaService } from './services/rozmowa-prywatna.service';
import { RoslinaService } from './services/roslina.service';
import { PostService } from './services/post.service';
import { KomentarzService } from './services/komentarz.service';
import { UzytkownikService } from './services/uzytkownik.service';
import { PracownikService } from './services/pracownik.service';
import { PowiadomienieService } from './services/powiadomienie.service';
import { DzialkaService } from './services/dzialka.service';
import { AuthenticationService } from './services/authentication.service';
import { OgrodService } from './services/ogrod.service';

/**
 * Module that provides all services and configuration.
 */
@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: [
    RoslinaWlasnaService,
    RozmowaPrywatnaService,
    RoslinaService,
    PostService,
    KomentarzService,
    UzytkownikService,
    PracownikService,
    PowiadomienieService,
    DzialkaService,
    AuthenticationService,
    OgrodService,
    ApiConfiguration
  ],
})
export class ApiModule {
  static forRoot(params: ApiConfigurationParams): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [
        {
          provide: ApiConfiguration,
          useValue: params
        }
      ]
    }
  }

  constructor( 
    @Optional() @SkipSelf() parentModule: ApiModule,
    @Optional() http: HttpClient
  ) {
    if (parentModule) {
      throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
    }
    if (!http) {
      throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
      'See also https://github.com/angular/angular/issues/20575');
    }
  }
}
