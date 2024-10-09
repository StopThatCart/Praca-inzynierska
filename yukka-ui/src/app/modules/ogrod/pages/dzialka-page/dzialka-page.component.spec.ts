import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DzialkaPageComponent } from './dzialka-page.component';

describe('DzialkaPageComponent', () => {
  let component: DzialkaPageComponent;
  let fixture: ComponentFixture<DzialkaPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DzialkaPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DzialkaPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
