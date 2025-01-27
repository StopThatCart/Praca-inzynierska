import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { error } from 'console';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlingService {

  constructor() { }

  handleErrors(err: any, errorMsg : string[]): string[] {
    console.log(err);
    errorMsg = [];

    if (err == null) {
      errorMsg.push('Wystąpił nieznany błąd');
      return errorMsg;
    }



    if (err.error) {
      if(err.error.validationErrors) {
        errorMsg = err.error.validationErrors
      } else if (err.error.error) {
        errorMsg.push(err.error.error);
      } else if (err.error.businessErrorDescription) {
        errorMsg.push(err.error.businessErrorDescription);
      }else {
        errorMsg.push(err.error);
      }
    } else if (err.status) {
      if(err.status === 403) {
        errorMsg.push('Status 403 - Brak uprawnień do wykonania tej operacji');
      } else if (err.status === 401) {
        errorMsg.push('Status 401 - Brak autoryzacji');
      } else if (err.status === 404) {
        errorMsg.push('Status 404 - Nie znaleziono zasobu');
      } else if (err.status === 500) {
        errorMsg.push('Status 500 - Błąd serwera');
      } else {
        errorMsg.push(err.status);
      }
    } else if (err.message) {
      errorMsg.push(err.message);
    } else {
      errorMsg.push(err);
    }
    return errorMsg;
  }

  handleResolverErrors(error: any, router : Router) {
    if (error.status === 404) {
      router.navigate(['/404']);
    } else if (error.status === 403) {
      router.navigate(['/403']);
    } else {
      router.navigate(['/404']);
    }
  }
}
