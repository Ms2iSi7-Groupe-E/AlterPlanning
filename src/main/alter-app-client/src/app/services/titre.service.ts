import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Observable } from 'rxjs/Observable';
import {AuthService} from "./auth.service";

@Injectable()
export class TitreService {Module

  constructor(private http: HttpClient) {
  }

  getTitres(): Observable<any> {
    return this.http
      .get('/api/titre', {headers: AuthService.getHeaders()});
  }
}
