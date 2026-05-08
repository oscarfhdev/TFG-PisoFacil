import { Injectable } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { Subject } from 'rxjs';

@Injectable()
export class SpanishPaginatorIntl implements MatPaginatorIntl {
  changes = new Subject<void>();

  firstPageLabel = 'Primera página';
  itemsPerPageLabel = 'Elementos por página:';
  lastPageLabel = 'Última página';
  nextPageLabel = 'Página siguiente';
  previousPageLabel = 'Página anterior';

  getRangeLabel(page: number, pageSize: number, length: number): string {
    if (length === 0) return 'Sin resultados';
    const amountPages = Math.ceil(length / pageSize);
    const start = page * pageSize + 1;
    const end = Math.min(page * pageSize + pageSize, length);
    return `${start} – ${end} de ${length}`;
  }
}
