import {Injectable} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {AuthService} from "./auth.service";
import {CalendarModel} from "../models/calendar.model";
import {CalendatrCoursModel} from "../models/calendar.cours.model";
import {HttpClient} from "@angular/common/http";
import {CalendarStateModel} from "../models/calendar-state.model";

@Injectable()
export class CalendarService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  addCoursToCalendar(idCalendar: number, body: CalendatrCoursModel): Observable<any> {
    return this.http
      .post('/api/calendar/' + idCalendar + '/cours', body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getCoursForCalendarInGeneration(idCalendar: number): Observable<any> {
    return this.http
      .get('/api/calendar/' + idCalendar + '/cours-for-generate-calendar', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getLinesForCalendar(idCalendar: number): Observable<any> {
    return this.http
      .get('/api/calendar/' + idCalendar + '/lines', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getCalendars(): Observable<any> {
    return this.http
      .get('/api/calendar', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  searchCalendars(params): Observable<any> {
    return this.http
      .get('/api/calendar/search', {headers: AuthService.getHeaders(), params: params})
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

  changeStateCalendar(idCalendar: number, body: CalendarStateModel) {
    return this.http
      .put('/api/calendar/' + idCalendar + '/change-state', body, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  deleteCalendar(idCalendar: number): Observable<any> {
    return this.http
      .delete('/api/calendar/' + idCalendar, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
