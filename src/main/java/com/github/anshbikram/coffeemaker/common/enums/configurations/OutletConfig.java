package com.github.anshbikram.coffeemaker.common.enums.configurations;

import com.github.anshbikram.coffeemaker.common.enums.exceptions.GenericException;
import java.util.Objects;

public class OutletConfig {

    private final int countN;

    public OutletConfig(Integer countN) {
        if (Objects.isNull(countN) || countN <= 0) {
            throw new GenericException("Invalid outlet configuration (outlet must be >= 1)");
        }

        this.countN = countN;
    }

    public int getCountN() {
        return countN;
    }
}
