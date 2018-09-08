import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';

@Injectable()
export class FormationService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  getFormations(): Observable<any> {
    return this.http
      .get('/api/formation', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getFormation(codeFormation: string): Observable<any> {
    return this.http
      .get('/api/formation/' + codeFormation, {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }

  getModules(codeFormation: string): Observable<any> {
    return this.http
      .get('/api/formation/' + codeFormation + '/modules', {headers: AuthService.getHeaders()})
      .catch((err) => this.authService.handleError(err));
  }
}
