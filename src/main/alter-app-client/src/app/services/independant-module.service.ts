import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";
import {IndependantModuleModel} from "../models/independant-module.model";

@Injectable()
export class IndependantModuleService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getCours(): Observable<any> {
    return this.http
      .get('/api/independant-module', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getCoursById(id: number): Observable<any> {
    return this.http
      .get('/api/independant-module/' + id, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  addCours(body: IndependantModuleModel): Observable<any> {
    return this.http
      .post('/api/independant-module', body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  updateCoursById(id: number, body: IndependantModuleModel): Observable<any> {
    return this.http
      .put('/api/independant-module/' + id, body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  deleteCoursById(id: number): Observable<any> {
    return this.http
      .delete('/api/independant-module/' + id, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
