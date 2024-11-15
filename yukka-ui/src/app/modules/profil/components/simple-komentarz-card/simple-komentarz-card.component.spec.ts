import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleKomentarzCardComponent } from './simple-komentarz-card.component';

describe('SimpleKomentarzCardComponent', () => {
  let component: SimpleKomentarzCardComponent;
  let fixture: ComponentFixture<SimpleKomentarzCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SimpleKomentarzCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SimpleKomentarzCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
