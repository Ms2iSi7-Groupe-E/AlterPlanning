import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AuthService} from "./auth.service";
import {Observable} from "rxjs/Observable";
import {ParameterModel} from "../models/parameter.model";

@Injectable()
export class ParameterService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getParamters(): Observable<any> {
    return this.http
      .get('/api/parameter', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getParameter(key: string): Observable<any> {
    return this.http
      .get('/api/parameter/' + key, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  updateParameter(key: String, body: ParameterModel): Observable<any> {
    return this.http
      .put('/api/parameter/' + key, body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

}
