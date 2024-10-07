@Service
public class ProductoServiceImpl implements IProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productoId));
    }

    @Override
    @Transactional
    public void saveProducto(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void updateProducto(Long productoId, Producto producto) {
        if (!productoRepository.existsById(productoId)) {
            throw new EntityNotFoundException("Product not found with ID: " + productoId);
        }
        producto.setId(productoId);
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void deleteProducto(Long productoId) {
        if (!productoRepository.existsById(productoId)) {
            throw new EntityNotFoundException("Product not found with ID: " + productoId);
        }
        productoRepository.deleteById(productoId);
    }

    @Override
    @Transactional
    public void aumentarCantidad(Long productoId, int cantidad) {
        Producto producto = getProductoById(productoId);
        producto.setCantidad(producto.getCantidad() + cantidad);
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void disminuirCantidad(Long productoId, int cantidad) {
        Producto producto = getProductoById(productoId);
        if (producto.getCantidad() < cantidad) {
            throw new IllegalArgumentException("Not enough quantity to decrease");
        }
        producto.setCantidad(producto.getCantidad() - cantidad);
        productoRepository.save(producto);
    }

    @Override
    public int getCantidad(Long productoId) {
        return getProductoById(productoId).getCantidad();
    }
}
