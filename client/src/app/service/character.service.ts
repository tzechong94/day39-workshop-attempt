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

  currentCharacter!: string
  
  constructor(private http: HttpClient) { }

  async getCharacters(searchInput: string) {
    const params = new HttpParams()
      .set("nameStartsWith", searchInput)

    return firstValueFrom(this.http.get("/api/characters", { params }))
    
  }

  getCharacterById(characterId: string){
    console.log(characterId)
    return firstValueFrom(this.http.get(`/api/character/${characterId}`))
  }

  postCommentById(characterId: string, newComment: string) {
    return firstValueFrom(this.http.post(`/api/character/${characterId}`, newComment))
  }

}
