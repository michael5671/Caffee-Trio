package com.ngntu10.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderStatus {
    private int status;
    //    WAIT_PAYMENT: 0,
    //    WAIT_DELIVERY: 1,
    //    DONE: 2,
    //    CANCEL: 3,
    //    ALL: 4
}
