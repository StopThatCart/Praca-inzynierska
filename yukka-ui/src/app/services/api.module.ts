/* tslint:disable */
/* eslint-disable */
import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';

import { UzytkownikService } from './services/uzytkownik.service';
import { RozmowaPrywatnaService } from './services/rozmowa-prywatna.service';
import { RoslinaService } from './services/roslina.service';
import { PostService } from './services/post.service';
import { KomentarzService } from './services/komentarz.service';
import { UzytkownikRoslinaService } from './services/uzytkownik-roslina.service';
import { DzialkaService } from './services/dzialka.service';
import { AuthenticationService } from './services/authentication.service';
import { SoilResourceService } from './services/soil-resource.service';

/**
 * Module that provides all services and configuration.
 */
@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: [
    UzytkownikService,
    RozmowaPrywatnaService,
    RoslinaService,
    PostService,
    KomentarzService,
    UzytkownikRoslinaService,
    DzialkaService,
    AuthenticationService,
    SoilResourceService,
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
