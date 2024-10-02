import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RozmowyListComponent } from './rozmowy-list.component';

describe('RozmowyListComponent', () => {
  let component: RozmowyListComponent;
  let fixture: ComponentFixture<RozmowyListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RozmowyListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RozmowyListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
