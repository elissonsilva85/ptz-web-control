import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'milisecondFormat'
})
export class MilisecondFormatPipe implements PipeTransform {

  transform(value: number): string {
    let result = "";

    let milisecond = value % 1000;
    let second = Math.floor(value/1000);
    
    if(second > 0) result = `${second}s `;
    result += `${milisecond}ms`;

    return result;
  }

}
