import { Injectable } from '@angular/core';
import { error } from 'console';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlingService {

  constructor() { }

  handleErrors(err: any, errorMsg : string[]): string[] {
    console.log(err);
    errorMsg = [];
    if(err.error.validationErrors) {
      errorMsg = err.error.validationErrors
    } else if (err.error.error) {
      errorMsg.push(err.error.error);
    } else if (err.error.businessErrorDescription) {
      errorMsg.push(err.error.businessErrorDescription);
    } else {
      errorMsg.push(err.message);
    }
    console.log("Error message: " + errorMsg);
    console.log("Error message: " + errorMsg.length);
    return errorMsg;
  }
}
