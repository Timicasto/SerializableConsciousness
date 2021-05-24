package cc.sukazyo.sericons.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyWrapper {

    public static class CustomEnergyStorage extends EnergyStorage {
        int averageInsertion = 0;
        int averageExtraction = 0;
        double averageDecayFactor = 0.5;
        public CustomEnergyStorage(int capacity) {
            super(capacity, capacity, capacity, 0);
        }

        public CustomEnergyStorage(int capacity, int maxTransfer) {
            super(capacity, maxTransfer, maxTransfer, 0);
        }

        public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract, 0);
        }

        public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
            super(capacity, maxReceive, maxExtract, energy);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                averageInsertion = (int)Math.round(averageInsertion * averageDecayFactor + received * (1 - averageDecayFactor));
            }
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                averageExtraction = (int)Math.round(averageExtraction * averageDecayFactor + extracted * (1 - averageDecayFactor));
            }
            return extracted;
        }

        @Override
        public int getEnergyStored() {
            return super.getEnergyStored();
        }

        @Override
        public int getMaxEnergyStored() {
            return super.getMaxEnergyStored();
        }

        @Override
        public boolean canExtract() {
            return super.canExtract();
        }

        @Override
        public boolean canReceive() {
            return super.canReceive();
        }

        public int getAverageInsertion() {
            return averageInsertion;
        }

        public int getAverageExtraction() {
            return averageExtraction;
        }

        public void setEnergyStored(int energy) {
            this.energy = energy;
        }

        public CustomEnergyStorage setDecayFactor(double value) {
            this.averageDecayFactor = value;
            return this;
        }

        public int getMaxReceive() {
            return this.maxReceive;
        }

        public int getMaxExtract() {
            return this.maxExtract;
        }

        public void deserializeNBT(CompoundTag compound) {
            this.energy = compound.getInt("Energy");
            this.capacity = compound.getInt("Capacity");
            this.maxReceive = compound.getInt("MaxReceive");
            this.maxExtract = compound.getInt("MaxExtract");
        }

        public void serializeNBT(CompoundTag compound) {
            compound.putInt("Energy", this.energy);
            compound.putInt("Capacity", this.capacity);
            compound.putInt("MaxReceive", this.maxReceive);
            compound.putInt("MaxExtract", this.maxExtract);
        }
    }
    public static class EnergyWrapperImpl implements IEnergyStorage {
        public int energy;
        final IEnergyConnector connector;
        public final Direction facing;

        public EnergyWrapperImpl(IEnergyConnector connector, Direction facing) {
            this.connector = connector;
            this.facing = facing;
        }

        @Override
        public int receiveEnergy(int i, boolean bl) {
            if (connector instanceof EnergyStorage) {
                return ((EnergyStorage) connector).receiveEnergy(i, bl);
            }
            return 0;
        }

        @Override
        public int extractEnergy(int i, boolean bl) {
            if (connector instanceof EnergyStorage) {
                return ((EnergyStorage) connector).extractEnergy(i, bl);
            }
            return 0;
        }

        @Override
        public int getEnergyStored() {
            if (connector instanceof EnergyStorage) {
                return ((EnergyStorage) connector).getEnergyStored();
            }
            return 0;
        }

        @Override
        public int getMaxEnergyStored() {
            if (connector instanceof EnergyStorage) {
                return ((EnergyStorage) connector).getMaxEnergyStored();
            }
            return 0;
        }

        @Override
        public boolean canExtract() {
            if (connector instanceof EnergyStorage) {
                return ((EnergyStorage) connector).getEnergyStored() > 0;
            }
            return false;
        }

        @Override
        public boolean canReceive() {
            if (connector instanceof EnergyStorage) {
                return ((EnergyStorage) connector).getMaxEnergyStored() > 0;
            }
            return false;
        }

        public EnergyWrapperImpl deserializeNBT(CompoundTag nbt) {
            this.energy = nbt.getInt("energy");
            if (energy > getMaxEnergyStored()) {
                energy = getMaxEnergyStored();
            }
            return this;
        }

        public CompoundTag serializeNBT(CompoundTag nbt) {
            if (energy < 0) {
                energy = 0;
            }
            nbt.putInt("energy", energy);
            return nbt;
        }
    }
}














