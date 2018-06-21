import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {CalendarModel} from "../models/calendar.model";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class CalendarService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getCalendars(): Observable<any> {
    return this.http
      .get('/api/calendar', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getCalendar(idCalendar: number): Observable<any> {
    return this.http
      .get('/api/calendar/' + idCalendar, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  addCalendar(body: CalendarModel): Observable<any> {
    return this.http
      .post('/api/calendar', body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
