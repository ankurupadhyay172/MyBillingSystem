package com.billing.mybilling.presentation


import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.billing.mybilling.BR
import com.billing.mybilling.R
import com.billing.mybilling.base.BaseFragment
import com.billing.mybilling.data.model.request.CommonRequestModel
import com.billing.mybilling.data.model.response.PendingOrders
import com.billing.mybilling.databinding.FragmentHomeBinding
import com.billing.mybilling.notification.sendNotificationToOrder
import com.billing.mybilling.presentation.adapter.PendingOrdersAdapter
import com.billing.mybilling.session.SessionManager
import com.billing.mybilling.utils.OrderStatus
import com.billing.mybilling.utils.OrderType
import com.billing.mybilling.utils.SelectedAction
import com.billing.mybilling.utils.setOrderStatus
import com.google.gson.Gson
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment:BaseFragment<FragmentHomeBinding,HomeViewModel>() {
    private val homeViewModel:HomeViewModel by activityViewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    @Inject
    lateinit var adapter: PendingOrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewDataBinding().rvOrders.adapter = adapter
        getViewDataBinding().manageProducts.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToViewCategoryListFragment())
        }
        getViewDataBinding().pendingOrders.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrders())
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())

        }
        getViewDataBinding().manageUsers.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStaffListFragment())
        }
        val number = "+916378999146"
        getViewDataBinding().contactUs.setOnClickListener {
//            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDeliveredOrdersFragment())

            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)

//            showRatingDialog(requireActivity(),layoutInflater,"Rate our app",{
//                showToast("rating is given ")
//            },{
//
//            }).show()
        }
        getViewDataBinding().add.setOnClickListener {
           findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddOrderFragment2())
//           createPdf()
        }

        getOrders()
        adapter.open = {id,item->
            homeViewModel.pendingOrders = item
            val orderOn = if (item!!.order_on==OrderType.TABLE.type) "Table No : "+item.table_no else "Packing"
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPendingOrdersDetailsFragment(id!!,orderOn,Gson().toJson(item)))

        }
        getViewDataBinding().layoutError.btnRetry.setOnClickListener {
            getOrders()
        }

        getViewDataBinding().swipeToRefresh.setOnRefreshListener {
            getOrders()
        }


        adapter.options = {id,item->
            AlertDialog.Builder(requireContext()).setTitle(R.string.option_order)
                .setItems(R.array.options_orders) { dialog, which ->
                   // showLoading(true)
                    getViewDataBinding().progressBar.visibility = View.VISIBLE
                    homeViewModel.updatePendingOrder(SelectedAction.UPDATE_STATUS.type, PendingOrders(order_id = id!!, order_status = which)).observe(viewLifecycleOwner){
                        it.getErrorIfExists()?.let {
//                            showLoading(false)
                            getViewDataBinding().progressBar.visibility = View.GONE
                            showToast("${it.message}")
                        }
                        it.getValueOrNull()?.let {
//                            showLoading(false)
                            getViewDataBinding().progressBar.visibility = View.GONE
                            getOrders()
                            sendNotificationToOrder("Order Updated",which.setOrderStatus(),{
                            },{

                            })
                            showToast(it.result)
                        }
                    }
                }.show()
        }

    }

    private fun getOrders() {
//        showLoading(true)
        getViewDataBinding().progressBar.visibility = View.VISIBLE
        homeViewModel.getPendingOrders(
            CommonRequestModel(sessionManager.getUser()?.business_id,
                OrderStatus.PENDING.status.toString())
        ).observe(viewLifecycleOwner){
            if (it.getErrorIfExists()==null)
                getViewDataBinding().error = it.getErrorIfExists()?.message

            it.getErrorIfExists()?.let {
//                showLoading(false)
                getViewDataBinding().progressBar.visibility = View.GONE
                getViewDataBinding().error = it.message
                getViewDataBinding().swipeToRefresh.isRefreshing = false
            }

            it.getValueOrNull()?.let {
//                showLoading(false)
                getViewDataBinding().progressBar.visibility = View.GONE
                getViewDataBinding().swipeToRefresh.isRefreshing = false
                adapter.submitList(it.result)
                if (it.status==0){
                    showLoading(false)
                    getViewDataBinding().isEmpty = true

                }

            }
        }

    }


    private fun createPdf() {
        // Creating a new PDF document
//        val pdfDocument = PdfDocument()
//
//        // Setting up paint objects for drawing shapes and text
//
//        // Setting up paint objects for drawing shapes and text
//        val paint = Paint()
//        val title = Paint()
//
//        // Setting page info for the PDF
//
//        // Setting page info for the PDF
//        val pageInfo = PageInfo.Builder(300, 600, 1).create()
//
//        // Starting a new page
//
//        // Starting a new page
//        val page = pdfDocument.startPage(pageInfo)
//
//        // Getting the canvas for the page
//
//        // Getting the canvas for the page
//        val canvas = page.canvas
//
//        // Adding the image to the PDF
//
//        // Adding the image to the PDF
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.billlogo3)
//        val scaledbmp = Bitmap.createScaledBitmap(bitmap, 50, 50, false)
//        canvas.drawBitmap(scaledbmp, 10f, 10f, paint)
//
//
//
//
//        // Adding the title text
//        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
//        title.textSize = 15f
//        title.color = Color.BLACK
//        canvas.drawText("Bill Details:", 10f, 200f, title)
//
//
//        // Adding the bill details
//        val billText = "Item 1: $10\nItem 2: $20\nTotal: $30"
//        canvas.drawText(billText, 10f, 220f, title)
//
//        // Finishing the page
//
//        // Finishing the page
//        pdfDocument.finishPage(page)

        // Saving the PDF to a file

        // Saving the PDF to a file
//        val file = File(Environment.getExternalStorageDirectory(), "bill.pdf")
        val pdfFileName = "example.pdf"
        val directory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val filePath = File(directory, pdfFileName)
//        try {
//            pdfDocument.writeTo(FileOutputStream(filePath))
//            Toast.makeText(
//                requireContext(),
//                "PDF file generated successfully.",
//                Toast.LENGTH_SHORT
//            ).show()
//            val packageName = "com.billing.mybilling"
//            val uri = FileProvider.getUriForFile(requireContext(), "${packageName}.provider", filePath)
////            val uri = Uri.fromFile(filePath)
//
////            val intent = Intent(Intent.ACTION_VIEW)
////            intent.setDataAndType(uri, "application/pdf")
////            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
////            startActivity(intent)
//
//            val number = "+916378999146"
//            val url = "https://api.whatsapp.com/send?phone=$number"
//            val i = Intent(Intent.ACTION_SEND)
//            i.putExtra(Intent.EXTRA_STREAM, uri)
//            i.putExtra(Intent.EXTRA_TEXT, "this is working")
//            i.type = "application/pdf"
////            i.data = Uri.parse(url)
//            i.putExtra("jid", "916378999146" + "@s.whatsapp.net")
//            startActivity(i)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.d("myLogRes", "createPdf: "+e.message)
//            Toast.makeText(requireContext(), "Failed to generate PDF file.", Toast.LENGTH_SHORT)
//                .show()
//        }
//
//
//        // Closing the PDF document
//        pdfDocument.close()


        try {
            val document = Document()
            PdfWriter.getInstance(document,FileOutputStream(filePath))
            document.open()
            document.setPageSize(PageSize.A4)

            document.addCreationDate()
            document.addAuthor("My Billing")
            document.addCreator("Ankur Upadhyay")

            //font setting
            val colorAccent = BaseColor(0,153,204,255)
            val fontSize = 11.0f
            val valueFontSize = 11.0f


            //Custom Font
            val fontName = BaseFont.createFont("res/font/poppins_medium.ttf","UTF-8",BaseFont.EMBEDDED)

            val packageName = "com.billing.mybilling"
            val uri = FileProvider.getUriForFile(requireContext(), "${packageName}.provider", filePath)

            val drawable = resources.getDrawable(R.drawable.billlogo3)
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            val image: Image = Image.getInstance(stream.toByteArray())
//            image.alignment = Image.MIDDLE
            document.add(image)

            val drawable1 = resources.getDrawable(R.drawable.cooking)
            val bitmapDrawable1 = drawable1 as BitmapDrawable
            val bitmap1 = bitmapDrawable1.bitmap
            val stream1 = ByteArrayOutputStream()
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1)
            val image1: Image = Image.getInstance(stream1.toByteArray())
            image1.alignment = Image.MIDDLE
            document.add(image1)


            //Create Title of document
            val titleFont = Font(fontName,13.0f,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document,"Order details",Element.ALIGN_CENTER,titleFont)

            //Add more
            val orderNumberFont = Font(fontName,fontSize,Font.NORMAL,colorAccent)
            addNewItem(document,"Order No:",Element.ALIGN_LEFT,orderNumberFont)

            val orderNumberValueFont = Font(fontName,valueFontSize,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document,"#717171",Element.ALIGN_LEFT,orderNumberValueFont)

            addLineSeparator(document)

            addNewItem(document,"Order Date",Element.ALIGN_LEFT,orderNumberFont)
            addNewItem(document,"15/05/2024",Element.ALIGN_LEFT,orderNumberValueFont)

            addLineSeparator(document)

            addNewItem(document,"Account Name:",Element.ALIGN_LEFT,orderNumberFont)
            addNewItem(document,"Ankur Upadhyay",Element.ALIGN_LEFT,orderNumberValueFont)

            addLineSeparator(document)

            //Add Product order detail
            addLineSpace(document)
            addNewItem(document,"Product Detail",Element.ALIGN_CENTER,titleFont)
            addLineSeparator(document)

            //Item1
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)",titleFont,orderNumberValueFont)
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont)

            addLineSeparator(document)

            //Item2
            addNewItemWithLeftAndRight(document,"Pizza 26","(0.0%)",titleFont,orderNumberValueFont)
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont)
            var i1 = 1
            while (i1<100){
                addNewItemWithLeftAndRight(document,"Pizza 26","(0.0%)",titleFont,orderNumberValueFont)
                addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titleFont,orderNumberValueFont)
                i1++
            }



            addLineSeparator(document)

            //Total
            addLineSpace(document)
            addLineSpace(document)

            addNewItemWithLeftAndRight(document,"Total","24000.0",titleFont,orderNumberValueFont)

            document.close()
            Toast.makeText(requireContext(), "Successfully Created pdf ", Toast.LENGTH_SHORT).show()



            val number = "+916378999146"
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_SEND)
            i.putExtra(Intent.EXTRA_STREAM, uri)
            i.putExtra(Intent.EXTRA_TEXT, "this is working")
            i.type = "application/pdf"
//            i.data = Uri.parse(url)
            i.putExtra("jid", "916378999146" + "@s.whatsapp.net")
            startActivity(i)
        }catch (e:Exception){
            e.printStackTrace()
            Log.d("myLogRes", "createPdf: "+e.message)
            Toast.makeText(requireContext(), "Failed to generate PDF file.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun addNewItemWithLeftAndRight(document: Document, textLeft: String, textRight: String, textLeftFont: Font,textRightFont:Font) {
        val chunkTextLeft = Chunk(textLeft,textLeftFont)
        val chunkTextRight = Chunk(textRight,textRightFont)
        val p = Paragraph(chunkTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTextRight)
        document.add(p)

    }

    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(document: Document) {
        document.add(Paragraph(""))
    }

    private fun addNewItem(document: Document, text: String, alignCenter: Int, font: Font) {
        val chunk = Chunk(text,font)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = alignCenter
        document.add(paragraph)
    }


    override fun getLayoutId() = R.layout.fragment_home
    override fun getBindingVariable() = BR.model
    override fun getViewModel() = homeViewModel
}