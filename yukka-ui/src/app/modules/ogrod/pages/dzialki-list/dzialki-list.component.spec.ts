import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DzialkiListComponent } from './dzialki-list.component';

describe('DzialkiListComponent', () => {
  let component: DzialkiListComponent;
  let fixture: ComponentFixture<DzialkiListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DzialkiListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DzialkiListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
