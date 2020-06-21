package application.models;
import javafx.scene.image.Image;

public class BaseImage extends Image{
	
	private Integer id;
	private String name;
	private Integer index;
	public boolean remove = false;
	
	public BaseImage(String string) {
		super(string);
	}
	
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

