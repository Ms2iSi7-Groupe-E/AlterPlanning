import { Injectable } from '@angular/core';

@Injectable()
export class ModelsService {

  constructor() { }

  getModels() {
    return [{id: 1, name: 'MODELE 1'},
      {id: 2, name: 'MODELE 2'}];
  }
}
