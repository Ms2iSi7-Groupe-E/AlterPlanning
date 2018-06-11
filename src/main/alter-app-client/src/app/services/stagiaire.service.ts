import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class StagiaireService {

  constructor(private http: HttpClient) { }

  getStagiaires(): Observable<any> {
    return this.http
      .get('/api/stagiaire', {headers: AuthService.getHeaders()});
  }

  getStagiaire(codeStagiaire: number): Observable<any> {
    return this.http
      .get('/api/stagiaire/' + codeStagiaire, {headers: AuthService.getHeaders()});
  }
}
