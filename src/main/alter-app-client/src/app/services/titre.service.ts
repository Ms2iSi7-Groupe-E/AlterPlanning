import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';

@Injectable()
export class TitreService {

  constructor(private http: HttpClient) {
  }

  getTitres(): Observable<any> {
    return this.http
      .get('/api/titre', {headers: AuthService.getHeaders()});
  }

  getFormations(codeTitre: string): Observable<any> {
    return this.http
      .get('/api/titre/' + codeTitre + '/formations', {headers: AuthService.getHeaders()});
  }
}
