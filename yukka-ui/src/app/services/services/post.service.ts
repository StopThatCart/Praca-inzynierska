/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addOcenaToPost } from '../fn/post/add-ocena-to-post';
import { AddOcenaToPost$Params } from '../fn/post/add-ocena-to-post';
import { addPost } from '../fn/post/add-post';
import { AddPost$Params } from '../fn/post/add-post';
import { findAllPosty } from '../fn/post/find-all-posty';
import { FindAllPosty$Params } from '../fn/post/find-all-posty';
import { findAllPostyByUzytkownik } from '../fn/post/find-all-posty-by-uzytkownik';
import { FindAllPostyByUzytkownik$Params } from '../fn/post/find-all-posty-by-uzytkownik';
import { findPostById } from '../fn/post/find-post-by-id';
import { FindPostById$Params } from '../fn/post/find-post-by-id';
import { findPostByIdCheck } from '../fn/post/find-post-by-id-check';
import { FindPostByIdCheck$Params } from '../fn/post/find-post-by-id-check';
import { OcenaResponse } from '../models/ocena-response';
import { PageResponsePostResponse } from '../models/page-response-post-response';
import { PostResponse } from '../models/post-response';
import { removePost } from '../fn/post/remove-post';
import { RemovePost$Params } from '../fn/post/remove-post';

@Injectable({ providedIn: 'root' })
export class PostService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addOcenaToPost()` */
  static readonly AddOcenaToPostPath = '/posty/oceny';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addOcenaToPost()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToPost$Response(params: AddOcenaToPost$Params, context?: HttpContext): Observable<StrictHttpResponse<OcenaResponse>> {
    return addOcenaToPost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addOcenaToPost$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addOcenaToPost(params: AddOcenaToPost$Params, context?: HttpContext): Observable<OcenaResponse> {
    return this.addOcenaToPost$Response(params, context).pipe(
      map((r: StrictHttpResponse<OcenaResponse>): OcenaResponse => r.body)
    );
  }

  /** Path part for operation `findAllPosty()` */
  static readonly FindAllPostyPath = '/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPosty()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPosty$Response(params?: FindAllPosty$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
    return findAllPosty(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPosty$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPosty(params?: FindAllPosty$Params, context?: HttpContext): Observable<PageResponsePostResponse> {
    return this.findAllPosty$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePostResponse>): PageResponsePostResponse => r.body)
    );
  }

  /** Path part for operation `addPost()` */
  static readonly AddPostPath = '/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addPost()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addPost$Response(params?: AddPost$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return addPost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addPost$Response()` instead.
   *
   * This method sends `multipart/form-data` and handles request body of type `multipart/form-data`.
   */
  addPost(params?: AddPost$Params, context?: HttpContext): Observable<PostResponse> {
    return this.addPost$Response(params, context).pipe(
      map((r: StrictHttpResponse<PostResponse>): PostResponse => r.body)
    );
  }

  /** Path part for operation `findPostById()` */
  static readonly FindPostByIdPath = '/posty/{post-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findPostById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostById$Response(params: FindPostById$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return findPostById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findPostById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostById(params: FindPostById$Params, context?: HttpContext): Observable<PostResponse> {
    return this.findPostById$Response(params, context).pipe(
      map((r: StrictHttpResponse<PostResponse>): PostResponse => r.body)
    );
  }

  /** Path part for operation `removePost()` */
  static readonly RemovePostPath = '/posty/{post-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `removePost()` instead.
   *
   * This method doesn't expect any request body.
   */
  removePost$Response(params: RemovePost$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return removePost(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `removePost$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  removePost(params: RemovePost$Params, context?: HttpContext): Observable<string> {
    return this.removePost$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `findPostByIdCheck()` */
  static readonly FindPostByIdCheckPath = '/posty/{post-id}/check';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findPostByIdCheck()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostByIdCheck$Response(params: FindPostByIdCheck$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return findPostByIdCheck(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findPostByIdCheck$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostByIdCheck(params: FindPostByIdCheck$Params, context?: HttpContext): Observable<PostResponse> {
    return this.findPostByIdCheck$Response(params, context).pipe(
      map((r: StrictHttpResponse<PostResponse>): PostResponse => r.body)
    );
  }

  /** Path part for operation `findAllPostyByUzytkownik()` */
  static readonly FindAllPostyByUzytkownikPath = '/posty/uzytkownik/{nazwa}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPostyByUzytkownik()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyByUzytkownik$Response(params: FindAllPostyByUzytkownik$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
    return findAllPostyByUzytkownik(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPostyByUzytkownik$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPostyByUzytkownik(params: FindAllPostyByUzytkownik$Params, context?: HttpContext): Observable<PageResponsePostResponse> {
    return this.findAllPostyByUzytkownik$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponsePostResponse>): PageResponsePostResponse => r.body)
    );
  }

}
