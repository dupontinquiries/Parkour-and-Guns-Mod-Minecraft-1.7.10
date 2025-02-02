// 
// Decompiled by Procyon v0.5.36
// 

package dme.sucaro.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;

public class EntityBullet extends Entity implements IProjectile {

	private static final double kVal = 0.10000000149011612;
	private static final double gVal = 0.007499999832361937D;

	private int field_145791_d;
	private int field_145792_e;
	private int field_145789_f;
	private Block field_145790_g;
	private int inData;
	private boolean inGround;
	public int canBePickedUp;
	public int arrowShake;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private double damage;
	private Item ammo;
	private int knockbackStrength;
	public boolean explodes;
	public float exSize;
	public int exDuration;
	private static final String __OBFID = "CL_00001715";

	public EntityBullet(final World p_i1753_1_) {
		super(p_i1753_1_);
		this.explodes = false;
		this.exDuration = -1;
		this.exSize = 0.0f;
		this.field_145791_d = -1;
		this.field_145792_e = -1;
		this.field_145789_f = -1;
		this.damage = 2.0;
		this.renderDistanceWeight = 10.0;
		this.setSize(0.5f, 0.5f);
	}

	public EntityBullet(final World p_i1754_1_, final double p_i1754_2_, final double p_i1754_4_,
			final double p_i1754_6_) {
		super(p_i1754_1_);
		this.explodes = false;
		this.exDuration = -1;
		this.exSize = 0.0f;
		this.field_145791_d = -1;
		this.field_145792_e = -1;
		this.field_145789_f = -1;
		this.damage = 2.0;
		this.renderDistanceWeight = 10.0;
		this.setSize(0.5f, 0.5f);
		this.setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
		this.yOffset = 0.0f;
	}

	public EntityBullet(final World w, final EntityLivingBase source, final EntityLivingBase target,
			final float p_i1755_4_, final float p_i1755_5_) {
		super(w);
		this.explodes = false;
		this.exDuration = -1;
		this.exSize = 0.0f;
		this.field_145791_d = -1;
		this.field_145792_e = -1;
		this.field_145789_f = -1;
		this.damage = 2.0;
		this.renderDistanceWeight = 10.0;
		this.shootingEntity = (Entity) source;
		if (source instanceof EntityPlayer) {
			this.canBePickedUp = 1;
		}
		this.posY = source.posY + source.getEyeHeight() - kVal - .3;
		final double d0 = target.posX - source.posX;
		final double d1 = target.boundingBox.minY + target.height / 3.0f - this.posY;
		final double d2 = target.posZ - source.posZ;
		final double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		if (d3 >= 1.0E-7) {
			final float f2 = (float) (Math.atan2(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
			final float f3 = (float) (-(Math.atan2(d1, d3) * 180.0 / 3.141592653589793));
			final double d4 = d0 / d3;
			final double d5 = d2 / d3;
			this.setLocationAndAngles(source.posX + d4, this.posY, source.posZ + d5, f2, f3);
			this.yOffset = 0.0f;
			final float f4 = (float) d3 * 0.2f;
			this.setThrowableHeading(d0, d1 + f4, d2, p_i1755_4_, p_i1755_5_);
		}
	}

	public EntityBullet(World w, EntityLivingBase elb, float p_i1756_3_, Item Ammo) {
		super(w);
		this.field_145791_d = -1;
		this.field_145792_e = -1;
		this.field_145789_f = -1;
		this.damage = 2.0;
		this.renderDistanceWeight = 10.0;
		this.shootingEntity = (Entity) elb;
		this.ammo = Ammo;
		this.explodes = false;
		this.exDuration = -1;
		this.exSize = 0.0f;
		if (elb instanceof EntityPlayer) {
			this.canBePickedUp = 1;
		}
		final Random rand = new Random();
		this.setSize(0.5f, 0.5f);
		this.setLocationAndAngles(elb.posX, elb.posY + elb.getEyeHeight(), elb.posZ, elb.rotationYaw,
				elb.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= kVal;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0f;
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, p_i1756_3_ * 1.5F, 1.0F);
	}

	protected void entityInit() {
		this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_,
			float p_70186_8_) {
		float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
		p_70186_1_ /= (double) f2;
		p_70186_3_ /= (double) f2;
		p_70186_5_ /= (double) f2;
		p_70186_1_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * gVal
				* (double) p_70186_8_;
		p_70186_3_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * gVal
				* (double) p_70186_8_;
		p_70186_5_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * gVal
				* (double) p_70186_8_;
		p_70186_1_ *= (double) p_70186_7_;
		p_70186_3_ *= (double) p_70186_7_;
		p_70186_5_ *= (double) p_70186_7_;
		this.motionX = p_70186_1_;
		this.motionY = p_70186_3_;
		this.motionZ = p_70186_5_;
		float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70186_3_, (double) f3) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_,
			float p_70056_8_, int p_70056_9_) {
		this.setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		this.setRotation(p_70056_7_, p_70056_8_);
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@SideOnly(Side.CLIENT)
	public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
		this.motionX = p_70016_1_;
		this.motionY = p_70016_3_;
		this.motionZ = p_70016_5_;

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70016_3_, (double) f) * 180.0D
					/ Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this
	 * Entity.
	 */
	public boolean canBeCollidedWith() {
		return false;
	}
	
	/**
	 * creates an explosion
	 */
	private void explosion() {
		//EntityTNTPrimed tnt = new EntityTNTPrimed(this.worldObj, this.motionX, 0.0, this.motionZ, (EntityLivingBase) this.shootingEntity);
		//tnt.fuse = 00;
		//this.worldObj.spawnEntityInWorld(tnt);
    	this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, this.exSize, false, false);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		super.onUpdate();

		if (this.ticksInAir % 3 == 0) {
			if (this.damage > 1) {
				this.damage -= 1;
			}
		}

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D
					/ Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D
					/ Math.PI);
		}

		Block block = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e, this.field_145789_f);

		if (block.getMaterial() != Material.air) {
			block.setBlockBoundsBasedOnState(this.worldObj, this.field_145791_d, this.field_145792_e,
					this.field_145789_f);
			AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.field_145791_d,
					this.field_145792_e, this.field_145789_f);

			if (axisalignedbb != null
					&& axisalignedbb.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if (this.arrowShake > 0) {
			--this.arrowShake;
		}

		if (this.inGround) {
			int j = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e, this.field_145789_f);

			if (block == this.field_145790_g && j == this.inData) {
				++this.ticksInGround;

				if (this.ticksInGround > this.exDuration || (this.ticksInGround == 2 && this.exDuration == -1)) // 1200
				{
					//if (this.explodes) {
					//	this.explosion();
					//}
					this.setDead();
				}
			} else {
				this.inGround = false;
				this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
				this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		} else {
			++this.ticksInAir;
			// simulate bullet drop
			//if (this.ticksInAir > 20) {
			//	this.addVelocity(0, -0.00005, 0);
			//}
			this.addVelocity(0, 0.045, 0);
			Vec3 vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
			Vec3 vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
					this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec31, vec3, false, true, false);
			vec31 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
			vec3 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY,
					this.posZ + this.motionZ);

			if (movingobjectposition != null) {
				vec3 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
					this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			int i;
			float f1;

			for (i = 0; i < list.size(); ++i) {
				Entity entity1 = (Entity) list.get(i);

				if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
					f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double) f1, (double) f1, (double) f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

					if (movingobjectposition1 != null) {
						double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null && movingobjectposition.entityHit != null
					&& movingobjectposition.entityHit instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

				if (entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer
						&& !((EntityPlayer) this.shootingEntity).canAttackPlayer(entityplayer)) {
					movingobjectposition = null;
				}
			}

			float f2;
			float f4;

			if (movingobjectposition != null) {
				if (movingobjectposition.entityHit != null) {
					f2 = MathHelper.sqrt_double(
							this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					int k = MathHelper.ceiling_double_int((double) f2 * this.damage);

					if (this.getIsCritical()) {
						k += this.rand.nextInt(k / 2 + 2);
					}

					DamageSource damagesource = null;

					if (this.shootingEntity == null) {
						damagesource = DamageSource.causeThrownDamage(this, this);
					} else {
						damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
					}

					if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
						movingobjectposition.entityHit.setFire(5);
					}

					if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float) k)) {
						if (movingobjectposition.entityHit instanceof EntityLivingBase) {
							EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

							if (!this.worldObj.isRemote) {
								//entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
							}

							if (this.knockbackStrength > 0) {
								f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

								if (f4 > 0.0F) {
									movingobjectposition.entityHit.addVelocity(
											this.motionX * (double) this.knockbackStrength * 0.6000000238418579D
													/ (double) f4,
											0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D
													/ (double) f4);
								}
							}

							if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
								EnchantmentHelper.func_151384_a(entitylivingbase, this.shootingEntity);
								EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity,
										entitylivingbase);
							}

							if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity
									&& movingobjectposition.entityHit instanceof EntityPlayer
									&& this.shootingEntity instanceof EntityPlayerMP) {
								((EntityPlayerMP) this.shootingEntity).playerNetServerHandler
										.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
							}
						}
						// play gun sound
						this.playSound("fireworks.largeBlast", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F)); // random.bowhit

						if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
							this.setDead();
						}
					} else {
						//this.motionX *= -0.10000000149011612D;
						//this.motionY *= -0.10000000149011612D;
						//this.motionZ *= -0.10000000149011612D;
						//this.rotationYaw += 180.0F;
						//this.prevRotationYaw += 180.0F;
						//this.ticksInAir = 0;
					}
				} else {
					this.field_145791_d = movingobjectposition.blockX;
					this.field_145792_e = movingobjectposition.blockY;
					this.field_145789_f = movingobjectposition.blockZ;
					this.field_145790_g = this.worldObj.getBlock(this.field_145791_d, this.field_145792_e,
							this.field_145789_f);
					this.inData = this.worldObj.getBlockMetadata(this.field_145791_d, this.field_145792_e,
							this.field_145789_f);
					this.motionX = (double) ((float) (movingobjectposition.hitVec.xCoord - this.posX));
					this.motionY = (double) ((float) (movingobjectposition.hitVec.yCoord - this.posY));
					this.motionZ = (double) ((float) (movingobjectposition.hitVec.zCoord - this.posZ));
					f2 = MathHelper.sqrt_double(
							this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
					this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
					this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
					this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
					this.playSound("fireworks.largeBlast", .3F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
					this.inGround = true;
					this.arrowShake = 7;
					this.setIsCritical(false);

					if (this.field_145790_g.getMaterial() != Material.air) {
						this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, this.field_145791_d,
								this.field_145792_e, this.field_145789_f, this);
					}
				}
			}

			if (this.getIsCritical()) {
				for (i = 0; i < 4; ++i) {
					this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double) i / 4.0D,
							this.posY + this.motionY * (double) i / 4.0D, this.posZ + this.motionZ * (double) i / 4.0D,
							-this.motionX, -this.motionY + 0.2D, -this.motionZ);
				}
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

			for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D
					/ Math.PI); this.rotationPitch
							- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f3 = 0.99F;
			f1 = 0.05F;

			if (this.isInWater()) {
				for (int l = 0; l < 4; ++l) {
					f4 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double) f4,
							this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4,
							this.motionX, this.motionY, this.motionZ);
				}

				f3 = 0.8F;
			}

			if (this.isWet()) {
				this.extinguish();
			}

			this.motionX *= (double) f3;
			this.motionY *= (double) f3;
			this.motionZ *= (double) f3;
			this.motionY -= (double) f1;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.func_145775_I();
			if (this.explodes) {
				if (this.ticksInAir > (int) (11.0 / 4.0 * this.exSize)) {
					if (this.ticksInAir < 55 && this.ticksInAir % 10 == 0) {
						this.explosion();
					}
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("xTile", (short) this.field_145791_d);
		p_70014_1_.setShort("yTile", (short) this.field_145792_e);
		p_70014_1_.setShort("zTile", (short) this.field_145789_f);
		p_70014_1_.setShort("life", (short) this.ticksInGround);
		p_70014_1_.setByte("inTile", (byte) Block.getIdFromBlock(this.field_145790_g));
		p_70014_1_.setByte("inData", (byte) this.inData);
		p_70014_1_.setByte("shake", (byte) this.arrowShake);
		p_70014_1_.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		p_70014_1_.setByte("pickup", (byte) this.canBePickedUp);
		p_70014_1_.setDouble("damage", this.damage);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		this.field_145791_d = p_70037_1_.getShort("xTile");
		this.field_145792_e = p_70037_1_.getShort("yTile");
		this.field_145789_f = p_70037_1_.getShort("zTile");
		this.ticksInGround = p_70037_1_.getShort("life");
		this.field_145790_g = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		this.inData = p_70037_1_.getByte("inData") & 255;
		this.arrowShake = p_70037_1_.getByte("shake") & 255;
		this.inGround = p_70037_1_.getByte("inGround") == 1;

		if (p_70037_1_.hasKey("damage", 99)) {
			this.damage = p_70037_1_.getDouble("damage");
		}

		if (p_70037_1_.hasKey("pickup", 99)) {
			this.canBePickedUp = p_70037_1_.getByte("pickup");
		} else if (p_70037_1_.hasKey("player", 99)) {
			this.canBePickedUp = p_70037_1_.getBoolean("player") ? 1 : 0;
		}
	}

	/**
	 * Called when this EntityThrowable hits a block or entity. Included special
	 * case of colliding with another bullet -> continue as usual
	 */
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null) {
			if (mop.entityHit.getClass() == EntityBullet.class) {
				//this.velocityChanged = true;
			}
			else if (mop.entityHit == this.shootingEntity) {
				this.setDamage(0);
				if (!this.worldObj.isRemote) {
					this.setDead();
				}
			} else {
				this.applyEntityCollision(mop.entityHit);
				mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity),
						(float) this.damage);
				
			}
		} else if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
	
	/*
	// code to move towards nearest player
    public void onUpdate()
    {
        super.onUpdate();

        if (this.field_70532_c > 0)
        {
            --this.field_70532_c;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.029999999329447746D;

        if (this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)).getMaterial() == Material.lava)
        {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
        double d0 = 8.0D;

        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100)
        {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0)
            {
                this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
            }

            this.xpTargetColor = this.xpColor;
        }

        if (this.closestPlayer != null)
        {
            double d1 = (this.closestPlayer.posX - this.posX) / d0;
            double d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0;
            double d3 = (this.closestPlayer.posZ - this.posZ) / d0;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D)
            {
                d5 *= d5;
                this.motionX += d1 / d4 * d5 * 0.1D;
                this.motionY += d2 / d4 * d5 * 0.1D;
                this.motionZ += d3 / d4 * d5 * 0.1D;
            }
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float f = 0.98F;

        if (this.onGround)
        {
            f = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ)).slipperiness * 0.98F;
        }

        this.motionX *= (double)f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double)f;

        if (this.onGround)
        {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.xpColor;
        ++this.xpOrbAge;

        if (this.xpOrbAge >= 6000)
        {
            this.setDead();
        }
    }
    */

	/**
	 * Applies a velocity to each of the entities pushing them away from each other.
	 * Args: entity
	 */
	@Override
	public void applyEntityCollision(Entity entity) {
		if (entity.riddenByEntity != this && entity.ridingEntity != this && entity.getClass() != EntityBullet.class) {
			double d0 = entity.posX - this.posX;
			double d1 = entity.posZ - this.posZ;
			double d2 = MathHelper.abs_max(d0, d1);

			if (d2 >= 0.009999999776482582D) {
				d2 = (double) MathHelper.sqrt_double(d2);
				d0 /= d2;
				d1 /= d2;
				double d3 = 1.0D / d2;

				if (d3 > 1.0D) {
					d3 = 1.0D;
				}

				d0 *= d3;
				d1 *= d3;
				d0 *= 0.05000000074505806D;
				d1 *= 0.05000000074505806D;
				d0 *= (double) (1.0F - this.entityCollisionReduction);
				d1 *= (double) (1.0F - this.entityCollisionReduction);
				//this.addVelocity(-d0, 0.0D, -d1);
				entity.addVelocity(d0, 0.0D, d1);
			}
		}
	}

	public void onCollideWithPlayer(final EntityPlayer player) {
		// catch all for self inflicted damage
		if (player == this.shootingEntity && this.ticksInAir > 2) {
			this.setDamage(0);
			this.noClip = true;
			this.setDead();
			return;
		}
		if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
			boolean flag = this.canBePickedUp == 1 || (this.canBePickedUp == 2 && player.capabilities.isCreativeMode);
			if (this.canBePickedUp == 1 && !player.inventory.addItemStackToInventory(new ItemStack(this.ammo, 1))) {
				flag = false;
			}
			if (flag) {
				this.playSound("random.pop", 0.2f,
						((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
				player.onItemPickup((Entity) this, 1);
				this.setDead();
			}
		}
	}

	protected boolean canTriggerWalking() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public float getShadowSize() {
		return 0.0f;
	}

	public void setDamage(final double d) {
		this.damage = d;
	}

	public double getDamage() {
		return this.damage;
	}

	public void setKnockbackStrength(final int s) {
		this.knockbackStrength = s;
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem() {
		return true;
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind it.
	 */
	public void setIsCritical(boolean crit) {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (crit) {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			this.dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	/**
	 * Whether the arrow has a stream of critical hit particles flying behind it.
	 */
	public boolean getIsCritical() {
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		return (b0 & 1) != 0;
	}
}
