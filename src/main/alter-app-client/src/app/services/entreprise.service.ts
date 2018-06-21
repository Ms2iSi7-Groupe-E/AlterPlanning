import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class EntrepriseService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getEntreprises(): Observable<any> {
    return this.http
      .get('/api/entreprise', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getEntreprise(codeEntreprise: number): Observable<any> {
    return this.http
      .get('/api/entreprise/' + codeEntreprise, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getStagiairesForEntreprise(codeEntreprise: number): Observable<any> {
    return this.http
      .get('/api/entreprise/' + codeEntreprise + '/stagiaires', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
