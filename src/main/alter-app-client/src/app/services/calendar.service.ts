import { Injectable } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CalendarService {

  constructor(private http: HttpClient) { }

  getCalendars(): Observable<any> {
    return this.http
      .get('/api/calendar', {headers: AuthService.getHeaders()});
  }

  getCalendar(idCalendar: number): Observable<any> {
    return this.http
      .get('/api/calendar/' + idCalendar, {headers: AuthService.getHeaders()});
  }
}
