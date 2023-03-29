import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Character } from '../model/models';

@Injectable({
  providedIn: 'root'
})
export class CharacterService {

  characterSearched: string = ""
  characters: Character[] = []

  constructor(private http: HttpClient) { }

  async getCharacters(searchInput: string) {
    const params = new HttpParams()
      .set("nameStartsWith", searchInput)

    return firstValueFrom(this.http.get("/api/characters", { params }))
    
  }
}
