import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";

@Injectable()
export class PromotionService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getPromotions(): Observable<any> {
    return this.http
      .get('/api/promotion', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getPromotion(codePromotion: string): Observable<any> {
    return this.http
      .get('/api/promotion/' + codePromotion, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getCourByCodePromotion(codePromotion: string): Observable<any> {
    return this.http
      .get('/api/promotion/' + codePromotion + '/cours', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
