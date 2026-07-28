package dtos;

public class CategoryTopProductDTO {
    private String categoryName;
    private String productName;
    private int totalSold;

    public CategoryTopProductDTO(String categoryName, String productName, int totalSold) {
        this.categoryName = categoryName;
        this.productName = productName;
        this.totalSold = totalSold;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }
}
