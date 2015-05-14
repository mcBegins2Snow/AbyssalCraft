/**
 * AbyssalCraft
 * Copyright 2012-2015 Shinoow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shinoow.abyssalcraft.api.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MaterializerRecipes {

	private static final MaterializerRecipes materializerBase = new MaterializerRecipes();
	/** The list of materialization results. */
	private Map<ItemStack, ItemStack> materializationList = new HashMap<ItemStack, ItemStack>();
	private Map<ItemStack, Float> experienceList = new HashMap<ItemStack, Float>();

	public static MaterializerRecipes materialization()
	{
		return materializerBase;
	}

	private MaterializerRecipes()
	{

	}

	public void transmutate(Block input, ItemStack output, float xp)
	{
		transmutate(Item.getItemFromBlock(input), output, xp);
	}

	public void transmutate(Item input, ItemStack output, float xp)
	{
		transmutate(new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE), output, xp);
	}

	public void transmutate(ItemStack input, ItemStack output, float xp)
	{
		materializationList.put(input, output);
		experienceList.put(output, Float.valueOf(xp));
	}

	/**
	 * Returns the materialization result of an item.
	 */
	public ItemStack getMaterializationResult(ItemStack par1ItemStack)
	{
		Iterator<?> iterator = materializationList.entrySet().iterator();
		Entry<?, ?> entry;

		do
		{
			if (!iterator.hasNext())
				return null;

			entry = (Entry<?, ?>)iterator.next();
		}
		while (!areStacksEqual(par1ItemStack, (ItemStack)entry.getKey()));

		return (ItemStack)entry.getValue();
	}

	private boolean areStacksEqual(ItemStack par1ItemStack, ItemStack par2ItemStack)
	{
		return par2ItemStack.getItem() == par1ItemStack.getItem() && (par2ItemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || par2ItemStack.getItemDamage() == par1ItemStack.getItemDamage());
	}

	public Map<ItemStack, ItemStack> getMaterializationList()
	{
		return materializationList;
	}

	public float getExperience(ItemStack par1ItemStack)
	{
		float ret = par1ItemStack.getItem().getSmeltingExperience(par1ItemStack);
		if (ret != -1) return ret;

		Iterator<?> iterator = experienceList.entrySet().iterator();
		Entry<?, ?> entry;

		do
		{
			if (!iterator.hasNext())
				return 0.0F;

			entry = (Entry<?, ?>)iterator.next();
		}
		while (!areStacksEqual(par1ItemStack, (ItemStack)entry.getKey()));

		return ((Float)entry.getValue()).floatValue();
	}
}