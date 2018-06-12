import {Injectable} from '@angular/core';
import {NgbDatepickerI18n} from '@ng-bootstrap/ng-bootstrap';

@Injectable()
export class DatepickerI18n extends NgbDatepickerI18n {

  private readonly I18N_VALUES = {
    'fr': {
      weekdays: ['Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa', 'Di'],
      months: ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Aou', 'Sep', 'Oct', 'Nov', 'Déc'],
    }
  };

  getWeekdayShortName(weekday: number): string {
    return this.I18N_VALUES.fr.weekdays[weekday - 1];
  }

  getMonthShortName(month: number): string {
    return this.I18N_VALUES.fr.months[month - 1];
  }

  getMonthFullName(month: number): string {
    return this.getMonthShortName(month);
  }
}
