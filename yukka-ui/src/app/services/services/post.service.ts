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
import { findAllPosts } from '../fn/post/find-all-posts';
import { FindAllPosts$Params } from '../fn/post/find-all-posts';
import { findAllPostyByUzytkownik } from '../fn/post/find-all-posty-by-uzytkownik';
import { FindAllPostyByUzytkownik$Params } from '../fn/post/find-all-posty-by-uzytkownik';
import { findPostByUuid } from '../fn/post/find-post-by-uuid';
import { FindPostByUuid$Params } from '../fn/post/find-post-by-uuid';
import { findPostByUuidCheck } from '../fn/post/find-post-by-uuid-check';
import { FindPostByUuidCheck$Params } from '../fn/post/find-post-by-uuid-check';
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

  /** Path part for operation `findAllPosts()` */
  static readonly FindAllPostsPath = '/posty';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllPosts()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPosts$Response(params?: FindAllPosts$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponsePostResponse>> {
    return findAllPosts(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllPosts$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllPosts(params?: FindAllPosts$Params, context?: HttpContext): Observable<PageResponsePostResponse> {
    return this.findAllPosts$Response(params, context).pipe(
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

  /** Path part for operation `findPostByUuid()` */
  static readonly FindPostByUuidPath = '/posty/{uuid}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findPostByUuid()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostByUuid$Response(params: FindPostByUuid$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return findPostByUuid(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findPostByUuid$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostByUuid(params: FindPostByUuid$Params, context?: HttpContext): Observable<PostResponse> {
    return this.findPostByUuid$Response(params, context).pipe(
      map((r: StrictHttpResponse<PostResponse>): PostResponse => r.body)
    );
  }

  /** Path part for operation `removePost()` */
  static readonly RemovePostPath = '/posty/{uuid}';

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

  /** Path part for operation `findPostByUuidCheck()` */
  static readonly FindPostByUuidCheckPath = '/posty/{uuid}/check';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findPostByUuidCheck()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostByUuidCheck$Response(params: FindPostByUuidCheck$Params, context?: HttpContext): Observable<StrictHttpResponse<PostResponse>> {
    return findPostByUuidCheck(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findPostByUuidCheck$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findPostByUuidCheck(params: FindPostByUuidCheck$Params, context?: HttpContext): Observable<PostResponse> {
    return this.findPostByUuidCheck$Response(params, context).pipe(
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
