public class Catalog {
	String item_number;
	double price;
	String size_description;
    String gender;
    String swatchColor1,swatchColor2;
    
	public String getSwatchColor1() {
		return swatchColor1;
	}

	public void setSwatchColor1(String swatchColor1) {
		this.swatchColor1 = swatchColor1;
	}

	public String getSwatchColor2() {
		return swatchColor2;
	}

	public void setSwatchColor2(String swatchColor2) {
		this.swatchColor2 = swatchColor2;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	String product_image;
	public String getProduct_image() {
		return product_image;
	}

	public void setProduct_image(String product_image) {
		this.product_image = product_image;
	}

	public String getItem_number() {
		return item_number;
	}

	public void setItem_number(String item_number) {
		this.item_number = item_number;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSize_description() {
		return size_description;
	}

	public void setSize_description(String size_description) {
		this.size_description = size_description;
	}

	@Override
	public String toString() {
		return "Catalog [item_number=" + item_number + ", price=" + price
				+ ", size_description=" + size_description + ", gender="
				+ gender + ", product_image=" + product_image + "]";
	}

	
}
