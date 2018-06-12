import {ConstraintModel} from "./constraint.model";

export class CalendarModel {
  public entrepriseId?: number;
  public stagiaireId?: number;
  public startDate?: Date;
  public endDate?: Date;
  public constraints: ConstraintModel[] = [];

  constructor() { }

}
