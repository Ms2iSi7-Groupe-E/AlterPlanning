import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CourService {

  constructor(private http: HttpClient) { }

  getCours(): Observable<any> {
    return this.http
      .get('/api/cours', {headers: AuthService.getHeaders()});
  }

  getCour(idCours: string): Observable<any> {
    return this.http
      .get('/api/cours/' + idCours, {headers: AuthService.getHeaders()});
  }
}
