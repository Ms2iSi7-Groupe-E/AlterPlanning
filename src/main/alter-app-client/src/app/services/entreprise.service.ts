import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class EntrepriseService {

  constructor(private http: HttpClient) { }

  getEntreprises(): Observable<any> {
    return this.http
      .get('/api/entreprise', {headers: AuthService.getHeaders()});
  }

  getEntreprise(codeEntreprise: number): Observable<any> {
    return this.http
      .get('/api/entreprise/' + codeEntreprise, {headers: AuthService.getHeaders()});
  }

  getStagiairesForEntreprise(codeEntreprise: number): Observable<any> {
    return this.http
      .get('/api/entreprise/' + codeEntreprise + '/stagiaires', {headers: AuthService.getHeaders()});
  }
}
