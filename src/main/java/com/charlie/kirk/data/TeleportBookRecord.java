package com.charlie.kirk.data;

import java.util.List;
import java.util.Objects;

public record TeleportBookRecord(List<TeleportPosition> positions) {
    public TeleportBookRecord(List<TeleportPosition> positions) {
        this.positions = positions;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.positions);
    }
}
